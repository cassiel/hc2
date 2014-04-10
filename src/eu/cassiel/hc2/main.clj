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

(defn coin [] (> (rand) 0.5))

(defn srand [] (- (* (rand) 2) 1))

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

(defn e-knees [] (let [rot0 (rand)
                       rotSpeed (* (srand) 0.5)
                       actions (shuffle ["spiral"
                                         "rebound"
                                         "expand"
                                         "swing"
                                         "jump"])
                       part (rand-nth ["spine"
                                       "fingers"
                                       "knees"
                                   "chin"])]
                   (fn [pos] {:vtag "diagonal"
                             :tcolour [0.4 0.7 1]
                             :children [{:htag "2"
                                         :tcolour (if (> pos 0.8) [1 0.8 0.4] [0.3 0.3 0.3])
                                         :children [{:children (map leaf actions)}
                                                    (leaf (select-by-pos pos ["obsessive"
                                                                              "obsessive"
                                                                              "obsessive"
                                                                              "cautious"
                                                                              "skeptical"
                                                                              "lustful"]))
                                                    (leaf part)]}]
                             :rotation (+ rot0 (* rotSpeed pos))})))

(defn hc3 [] (let [rot0 (rand)
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
                         :rotation 0.75 #_ (+ rot0 (* rotSpeed pos))})))

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

(defn cue-box [state box cue len]
  (a/cue-anim state
              (fn [state] (fn [state pos] [(tag-progress (box pos) [0.0 1.0] pos) state]))
              cue
              len))
