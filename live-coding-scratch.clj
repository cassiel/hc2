;; To connect:
;;    View->Connections
;;    Add Connection
;;    Clojure (remote nREPL)
;;    bakirkoy.cassiel.com:9000

;; Then:


;; Evaluate this first:
(ns eu.cassiel.hc2.main)


;; Fire when ready! These take effect in e_knees only:
(defn live-jumper-1 [] "jump")
(defn live-jumper-2 [] "spiral")
(defn live-jumper-3 [] "rebound")
(defn live-jumper-4 [] "expand")
(defn live-jumper-5 [] "swing")


;; Fire this statement to see the current phrase (if any). This will
;; pretty-print *into the console* at the bottom (View->Console).
;; (Look at :presence - this is the level of the text. 1.0 is white,
;; 0.2 is "disabled" grey.)

(clojure.pprint/pprint (__examine))


;; Notes for Nick:
;; On bakirkoy.cassiel.com
;;    screen ~/tcpforward/tcpforward -v -l 0.0.0.0:9000 -l 0.0.0.0:9001
;; On localhost, when Field running:
;;    ~/tcpforward/tcpforward -v -c bakirkoy.cassiel.com:9001 -c localhost:4555
;; Remote nREPL connection point:
;;    bakirkoy.cassiel.com:9000
