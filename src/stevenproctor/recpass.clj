(ns stevenproctor.recpass
  (:gen-class)
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [stevenproctor.recpass.app :refer [app]]))

(defn -main
  [& _args]
  (run-jetty app {:port 3456 :join? false}))
