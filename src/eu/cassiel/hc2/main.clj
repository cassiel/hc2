(ns eu.cassiel.hc2.main
  (:require (eu.cassiel.hc2 [animo :as a])))

(def test_tree {:tag "X>"
                :children [{:leaf "Amy"}
                           {:leaf "Brenda"}
                           {:leaf "Brennie"}
                           {:children [{:leaf "==="} {:leaf "..."}]}
                           {:leaf "FINALLY"}]})

(def test_tree_2 {:tag "3 *"
                  :children [{:tag "RIGHT" :children [{:leaf "jab melt slide"}
                                                      {:leaf "float expand"}
                                                      {:leaf "whisk"}]}
                             {:leaf "strong"}
                             {:leaf "leg"}]})

(defn bang [state]
  (a/cue-anim state a/null-anim 500 2000))

(defn cue-box [state box cue len]
  (a/cue-anim state
              (fn [state] (fn [state pos] [box state]))
              cue
              len))
