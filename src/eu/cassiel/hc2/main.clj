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

(defn hc1 [] {:tag "X"
                 :children [{:tag "Y"
                             :children (map leaf ["spiral rebound"
                                                  "expand swing"
                                                  "jump"])}
                            (leaf "obsessive")
                            (leaf "spine")]})

(defn hc2 [] {:tag "X"
                 :children [{:tag "Y"
                             :children (map leaf ["rotate isolate"
                                                  "whisk pull"
                                                  "flip"])}
                            (leaf "quick")
                            (leaf "arms")]})

(defn hc3 [] {:tag "X"
                 :children [{:tag "Y"
                             :children (map leaf ["melt slide"
                                                  "rebound float"
                                                  "swing hover"])}
                            (leaf "sustain")
                            (leaf "legs")]})

(defn hc4 [] {:tag "X"
              :children [{:tag "Y"
                          :children (map leaf ["rise expand"
                                               "pendulum glide"
                                               "float"])}
                         (leaf "hard")
                         (leaf "shoulder")]})

(defn tag-presence [tree pos]
  (when-let [children (:children tree)]
    ;; Turn presence to 1.0 along children according to animation position:
    (let [c (count children)
          which-are-on (int (inc (* c pos)))]
      (assoc tree
        :children
        (map-indexed (fn [i x] (assoc x :presence (if (< i which-are-on) 1.0 0.0)))
                     children)))))

(defn bang [state]
  (a/cue-anim state a/null-anim 500 2000))

(defn cue-box [state box cue len]
  (a/cue-anim state
              (fn [state] (fn [state pos] [(tag-presence box pos) state]))
              cue
              len))
