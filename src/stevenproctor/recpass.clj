(ns stevenproctor.recpass
  (:gen-class)
  (:require [clojure.tools.cli :refer [parse-opts]]
            [ring.adapter.jetty :refer [run-jetty]]
            [stevenproctor.recpass.app :refer [app]]))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :default 3456
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-h" "--help"]])

(defn -main
  [& args]
  (let [opts (parse-opts args cli-options)
        help (get-in opts [:options :help])
        port (get-in opts [:options :port])]
    (if help
      (println (:summary opts))
      (run-jetty app {:port port}))))
