(ns stevenproctor.recpass.async-utils
  (:require
   [clojure.core.async :as async :refer [alts!!]]))

(defn all
  ([err-chan result-channels] (all err-chan result-channels {}))
  ([err-chan result-channels {:keys [timeout] :or {timeout 5000} :as _opts}]
   (let [results-chan (->> result-channels
                           async/merge
                           (async/reduce conj []))
         timeout-chan (async/timeout timeout)
         [res c] (alts!! [results-chan err-chan timeout-chan])]
     (cond
       (= c err-chan) (throw res)
       (= c timeout-chan) (throw (ex-info "timed out" {}))
       :else res))))
