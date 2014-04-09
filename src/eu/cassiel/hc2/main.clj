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


(defn hc1 [] (let [rot (rand)] (fn [pos] {:tag "X"
                                         :children [{:tag "Y"
                                                     :children (map leaf ["spiral rebound"
                                                                          "expand swing"
                                                                          "jump"])}
                                                    (leaf "obsessive")
                                                    #_ {:tag "X" :children [(leaf (select-by-pos pos ["A" "B" "C" "D" "E" "F" "G" "H"]))]}
                                                    (leaf "spine")]
                                         :rotation rot})))

(defn hc2 [] (let [rot (rand)] (fn [pos] {:tag "X"
                                         :children [{:tag "Y"
                                                     :children (map leaf ["rotate isolate"
                                                                          "whisk pull"
                                                                          "flip"])}
                                                    (leaf "quick")
                                                    {:tag "X" :children (map leaf ["A" "B" "C" "D" "E" "F"])}
                                                    (leaf "arms")]
                                         :rotation rot})))

(defn hc3 [] (let [rot (rand)] (fn [pos] {:tag "X"
                                         :children [{:tag "Y"
                                                     :children (map leaf ["melt slide"
                                                                          "rebound float"
                                                                          "swing hover"])}
                                                    (leaf "sustain")
                                                    (leaf "legs")]
                                         :rotation rot})))

(defn hc4 [] (let [rot (rand)] (fn [pos] {:tag "X"
                                         :children [{:tag "Y"
                                                     :children (map leaf ["rise expand"
                                                                          "pendulum glide"
                                                                          "float"])}
                                                    (leaf "hard")
                                                    (leaf "shoulder")]
                                         :rotation rot})))

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
                                    (assoc :presence (if (< i which-are-on) 1.0 0.0))
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
