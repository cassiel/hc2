(ns eu.cassiel.hc2.main
  (:require (eu.cassiel.hc2 [animo :as a])))

(defn tree-1 [] {:tag "X>"
                 :children [{:leaf "Amy"}
                            {:leaf "Brenda"}
                            {:leaf "Brennie"}
                            {:children [{:leaf "==="} {:leaf "..."}]}
                            {:leaf "FINALLY"}]})

(defn tree-2 [] {:tag "3 *"
                 :children [{:tag "RIGHT" :children [{:leaf "jab melt slide"}
                                                     {:leaf "float expand"}
                                                     {:leaf "whisk"}]}
                            {:leaf "strong"}
                            {:leaf "leg"}]})

(defn tree-3 [] {:tag "X"
                 :children (map (fn [i] {:leaf (str (inc i))})
                                (range 10))})

(defn leaf [x] {:leaf x})

(defn select-by-pos [pos a]
  (nth a (int (* pos (count a)))))

(defn live-jumper-1 [] "jump")
(defn live-jumper-2 [] "spiral")
(defn live-jumper-3 [] "rebound")
(defn live-jumper-4 [] "expand")
(defn live-jumper-5 [] "swing")

(defn my-rot [pos] (* 0 (rand)))

(defn coin [] (> (rand) 0.5))

(defn srand [] (- (* (rand) 2) 1))

(defn wrap-call [f]
  (str
   (try (f)
        (catch Exception e "---"))))

(defn euro [] (let [rot0 (rand)
                    rotSpeed (* (srand) 0.5)
                    whisk-or-pull (rand-nth ["whisk pull"
                                             "pull whisk"])
                    arms-or-legs (rand-nth ["arms" "legs"])
                    quick-or-slow (rand-nth ["quick" "slow"])
                    bf (rand-nth ["backwards" "forwards"])
                    times (str (int (+ 5 (* (rand) 3))))]
                (fn [pos] {:vtag bf
                          :tcolour [0.4 0.7 1]
                          :children [{:htag times
                                      :tcolour (if (> pos 0.8) [1 0.8 0.4] [0.3 0.3 0.3])
                                      :children [{:children (map leaf ["rotate isolate"
                                                                       whisk-or-pull
                                                                       "flip"])}
                                                 (leaf quick-or-slow)
                                                 (leaf arms-or-legs)]}]
                          :rotation (+ rot0 (* rotSpeed pos))})))

(defn e_knees [] (let [rot0 (rand)
                       rotSpeed (* (srand) 0.5)
                       actions (shuffle [:H1 :H2 :H3 :H4 :H5])
                       part (rand-nth ["spine"
                                       "fingers"
                                       "knees"
                                   "chin"])]
                   (fn [pos] {:vtag "diagonal"
                             :tcolour [0.4 0.7 1]
                             :children [{:htag "2"
                                         :tcolour (if (> pos 0.8) [1 0.8 0.4] [0.3 0.3 0.3])
                                         :children [{:children (map leaf
                                                                    (replace
                                                                     {:H1 (wrap-call live-jumper-1)
                                                                      :H2 (wrap-call live-jumper-2)
                                                                      :H3 (wrap-call live-jumper-3)
                                                                      :H4 (wrap-call live-jumper-4)
                                                                      :H5 (wrap-call live-jumper-5)}
                                                                     actions))}
                                                    (leaf (select-by-pos pos ["obsessive"
                                                                              "obsessive"
                                                                              "obsessive"
                                                                              "cautious"
                                                                              "skeptical"
                                                                              "lustful"]))
                                                    (leaf part)]}]
                             :rotation (+ rot0 (* rotSpeed pos))})))

(defn switcher [] (let [rot0 (rand)
                        rotSpeed (* (srand) 0.5)
                        qualities (shuffle ["sustain" "hard" "soft" "sudden"])
                        parts (shuffle ["legs" "shoulders" "sacrum" "chest"])
                        times (inc (rand-nth (range 5)))]
                    (fn [pos] {:vtag "left"
                              :tcolour [0.4 0.7 1]
                              :children [{:htag (str times)
                                          :tcolour (if (> pos 0.8) [1 0.8 0.4] [0.3 0.3 0.3])
                                          :children [(leaf (select-by-pos pos ["melt slide"
                                                                               "rebound float"
                                                                               "swing hover"]))
                                                     (leaf (select-by-pos pos (concat (repeat 2 "|")
                                                                                      ["sustain"
                                                                                       "hard"
                                                                                       "soft"
                                                                                       "sudden"])))
                                                     (leaf (select-by-pos pos (concat (repeat 8 "|")
                                                                                      ["legs"
                                                                                       "shoulders"
                                                                                       "sacrum"
                                                                                       "chest"])))]}]
                              :rotation (+ rot0 (* rotSpeed pos))
                              :rotationx 0})))

(defn original [] (let [rot0 (rand)
                        rotSpeed (* (srand) 0.5)]
                    (fn [pos] {:vtag "right"
                              :tcolour [0.4 0.7 1]
                              :children [{:htag "3"
                                          :tcolour (if (> pos 0.8) [1 0.8 0.4] [0.3 0.3 0.3])
                                          :children [{:children (map leaf ["rise expand"
                                                                           "pendulum glide"
                                                                           "float"])}
                                                     (leaf "hard")
                                                     (leaf "shoulder")]}]
                              :rotation (+ rot0 (* rotSpeed pos))
                              :rotationx 0})))

(defn simple [] (fn [pos] {:vtag (when (> pos 0.5) "FREE ME")
                          :tcolour [1 0.8 0.4]
                          :children (map leaf ["Symphony" "Destiny" "Rhapsody" "Melody" "Harmony"])}))

(defn filling [] ["FILLING"])

(defn template [] (fn [pos] {:vtag "TEMPLATE"
                            :tcolour [1 0.8 0.4]
                            :children (map leaf (map str (filling)))}))

(defn call-subdivide [pos fn-list]
  (let [c (count fn-list)
        super-pos (* pos c)
        f (nth fn-list (int super-pos))]
    (f (mod super-pos 1)))) ;; Not quite right? Ah: :progress gets planted later.

(defn uberbox []
  (let [euro-1 (euro)
        euro-2 (euro)
        knees-1 (e_knees)
        knees-2 (e_knees)
        knees-3 (e_knees)
        switcher (switcher)
        original (original)
        rot0 (rand)
        rotSpeed (* (srand) 0.5)]
    (fn [pos] {:children [(call-subdivide pos [euro-1 euro-2 knees-1 knees-2 knees-3 switcher original])]
              :rotation (+ rot0 (* rotSpeed pos))})))

(defn clip [x]
  (min 1.0 (max 0.0 x)))

(defn norm-in-range [[p1 p2] pos]
  (clip (/ (- pos p1) (- p2 p1))))

(defn tag-progress
  "Called at top level of tree, so its bracket, tag etc. are always visible (default presence is 1.0)."
  [tree [p1 p2] pos]
  (if-let [children (:children tree)]
    ;; Turn presence to 1.0 along children according to animation position:
    (let [pos' (norm-in-range [p1 p2] pos)
          c (count children)
          interval (/ (- p2 p1) c)
          which-are-on (int (inc (* c pos')))]
      (assoc tree
        :children
        (map-indexed (fn [i x] (let [p1' (+ p1 (* i interval))
                                    p2' (+ p1' interval)]
                                (-> x
                                    (tag-progress [p1' p2'] pos)
                                    (assoc :presence (if (< i which-are-on) 1.0 0.2)
                                           :progress (norm-in-range [p1' p2'] pos'))
                                    )))
                     children)
        :progress pos))
    tree))

(defn bang [state]
  (a/cue-anim state a/null-anim 500 2000))

(defn cue_box [state box cue len]
  (a/cue-anim state
              (fn [state] (fn [state pos] [(tag-progress (box pos) [0.0 1.0] pos) state]))
              cue
              len))

;; For Kate:
(def ^:dynamic *saved* (atom nil))
(defn __save [state] (reset *saved* state))
(defn __examine [] (deref *saved))
