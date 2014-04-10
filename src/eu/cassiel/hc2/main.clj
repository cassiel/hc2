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

(defn jumper [] "jump")

(defn my-rot [pos] (* 0 (rand)))

(defn hc1 [] (let [rot (rand)] (fn [pos] {:htag "2"
                                         :tcolour (if (> pos 0.8) [1 0.8 0.4] [0.3 0.3 0.3])
                                         :tcolourxxx (repeat 3 pos)
                                         :children [{:children (map leaf ["spiral rebound"
                                                                          "expand swing"
                                                                          (jumper)])}
                                                    (leaf "obsessive")
                                                    {:children [(leaf (select-by-pos pos ["A" "B" "C" "D" "E" "F" "G" "H"]))]}
                                                    (leaf "spine")]
                                         :rotation (my-rot pos)})))

(defn coin [] (> (rand) 0.5))

(defn hc2 [] (let [rot (rand)
                   whisk-or-pull (if (coin)
                                   "whisk pull"
                                   "pull whisk")
                   bf (if (coin) "backwards" "forwards")
                   times (str (int (+ 5 (* (rand) 3))))]
               (fn [pos] {:vtag bf
                         :tcolour [0.2 0.3 1]
                         :children [{:htag times
                                     :tcolour (if (> pos 0.8) [1 0.8 0.4] [0.3 0.3 0.3])
                                     :children [{:children (map leaf ["rotate isolate"
                                                                      whisk-or-pull
                                                                      "flip"])}
                                                (leaf "quick")
                                                (leaf "arms")]}]
                         :rotation rot})))

(defn hc3 [] (let [rot (rand)] (fn [pos] {:vtag "diagonal"
                                         :children [{:htag "1"
                                                     :children [{:children (map leaf ["melt slide"
                                                                                      "rebound float"
                                                                                      "swing hover"])}
                                                                (leaf "sustain")
                                                                (leaf "legs")]}]
                                         :rotation rot})))

(defn hc4 [] (let [rot (rand)] (fn [pos] {:vtag "right"
                                         :children [{:htag "3"
                                                     :children [{:children (map leaf ["rise expand"
                                                                                      "pendulum glide"
                                                                                      "float"])}
                                                                (leaf "hard")
                                                                (leaf "shoulder")]}]
                                         :rotation rot})))

(defn simple [] (fn [pos] {:vtag (when (> pos 0.5) "FREE ME")
                          :tcolour [1 0.8 0.4]
                          :children (map leaf ["Symphony" "Destiny" "Rhapsody" "Melody" "Harmony"])}))

(defn clip [x]
  (min 1.0 (max 0.0 x)))

(defn norm-in-range [[p1 p2] pos]
  (clip (/ (- pos p1) (- p2 p1))))

(defn tag-presence
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
                                    (assoc :presence (if (< i which-are-on) 1.0 0.2))
                                    (tag-presence [p1' p2'] pos))))
                     children)))
    tree))

(defn bang [state]
  (a/cue-anim state a/null-anim 500 2000))

(defn cue-box [state box cue len]
  (a/cue-anim state
              (fn [state] (fn [state pos] [(tag-presence (box pos) [0.0 1.0] pos) state]))
              cue
              len))
