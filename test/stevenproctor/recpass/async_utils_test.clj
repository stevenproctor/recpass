(ns stevenproctor.recpass.async-utils-test
  (:require
   [clojure.core.async :refer [chan go]]
   [clojure.test :refer :all]
   [stevenproctor.recpass.async-utils :refer :all]))

(deftest all-throws-timed-out-exception-if-threshold-reached
  (try
    (all (chan 1) [(chan 1)] {:timeout 10})
    (is false "should have timed out instead of getting here.")
    (catch Throwable err
      (is (= "timed out" (ex-message err))))))

(deftest all-throws-if-error-channel-gets-item
  (let [the-exception (ex-info "error-channel-val" {})
        err-chan (go the-exception)]
    (try
      (all err-chan [(chan 1)] {:timeout 1000})
      (is false "should have gotten error thrown instead of getting here.")
      (catch Throwable err
        (is (= the-exception err))))))

(deftest all-returns-results-when-no-error-and-timeout-not-reached
  (let [the-range (range 100)
        ->keyword (fn [x] (-> x str keyword))
        results-channels (map #(go (->keyword %)) the-range)
        res (all (chan 1) results-channels {:timeout 1000})]
    (is (= (count results-channels)
           (count res)))
    (is (= (set res)
           (set (mapv ->keyword the-range))))))


