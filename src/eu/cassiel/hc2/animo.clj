(ns eu.cassiel.hc2.animo
  "Fairly generic millisecond-based animator.")

(def initial-state {:anims []
                    :time 0})

(defn null-anim
  "Example null animator. The generator is called with the state (which might have
   additional fields) and returns an actual animation function. This function
   takes the (current) state and normalised timeline pos, and returns some output
   and a new state."
  [state]
  (fn [state pos] [pos state]))

(defn cue-anim
  "Add a plate to the currently running ones (at the end), ready to start."
  [state generator cue t-msec]
  (update-in state
             [:anims]
             #(conj % {:anim (generator state)
                       :cue cue        ; Replaced by :start msec on `draw` pass.
                       :length t-msec})))

(defn set-start-times
  "In the `draw` routine, set start times to this frame if they're nil."
  [state]
  (let [now (:time state)]
    (update-in state
               [:anims]
               #(map
                 (fn [entry] (if-let [c (:cue entry)]
                              (-> entry
                                  (assoc :start (+ now c))
                                  (dissoc :cue))
                              entry))
                 %))))

(defn purge
  "Purge generators which are expired."
  [state]
  (let [now (:time state)]
    (update-in state
               [:anims]
               #(filter
                 (fn [entry] (> (+ (:start entry) (:length entry))
                               now))
                 %))))

(defn render-item
  "Render an item in thie state. Returns the output paired with the new state."
  [state {:keys [anim start length]}]
  (let [now (:time state)]
    (if (<= start now)
      (anim state (float (/ (- (:time state) start) length)))
      [nil state])))

(defn render-anims
  "Actually call the rendering functions for frames that are in range, returning
   the outputs, plus a new state (possibly with purged entries)."
  [state ms]
  (let [state (-> state
                  (assoc :time ms)
                  (set-start-times)
                  (purge))]
    (reduce (fn [[outputs state] anim]
              (let [[x state'] (render-item state anim)]
                [(cons x outputs) state']))
            [nil state]
            (:anims state))))
