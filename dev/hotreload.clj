(ns hotreload
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [stevenproctor.recpass.app :refer [app]])
  (:gen-class))

(def dev-handler
  (wrap-reload #'app))

(defn jetty!
  ([] (jetty! {}))
  ([opts]
   (run-jetty dev-handler (merge {:port 3456}
                                 opts))))

(defn -main [& args]
  (jetty!))

(comment
  (defonce server (jetty! (:join false))))
