(ns stevenproctor.recpass.giantbomb
  (:require
   [cheshire.core :as json]
   [clj-http.client :as http]
   [clojure.core.async :as async :refer [close! chan put!]]
   [stevenproctor.recpass.async-utils :as async-utils]))

(defn parsed-results [resp]
  (-> resp
      :body
      (json/parse-string true)
      :results))

(def normalize-results-xf
  (map
   (fn [result]
     (merge (select-keys result [:guid :id :name])
            {:image-url (get-in result [:image :small_url])
             :image-icon-url (get-in result [:image :icon_url])}))))

(defn query-endpoint
  "Synchronously queries the GiantBomb API to find games with
  the name given in game-name."
  [api-key game-name]
  (http/get
   "https://www.giantbomb.com/api/games/"
   {:headers {:user-agent "RecPass breakable-toy programming problem client for testing library usage"}
    :query-params {:api_key api-key
                   :format "json"
                   :limit 10
                   :field_list "guid,id,image,name"
                   :sort "name:asc"
                   :filter (str "name:" game-name)}}))

(defn game-by-id
  "Asynchronously gets a games information by the game guid.

  Takes the api key, a channel to write errors to if the request
  fails, and the game's guid identifier for the GiantBomb API.

  Returns a channel with the parsed JSON body of the request as
  a Clojure data structure."
  [api-key err-chan guid]
  (let [c (chan 1)]
    (http/get
     (str "https://www.giantbomb.com/api/game/" guid "/")
     {:headers {:user-agent "RecPass breakable-toy programming problem client for testing library usage"}
      :async? true
      :query-params {:api_key api-key
                     :format "json"
                     :field_list "guid,id,image,name"}}
     (fn [response]
       (put! c (parsed-results response))
       (close! c))
     (fn [exception]
       (close! c)
       (put! err-chan exception)))
    c))

(defn games-by-ids
  "Concurrently fetch games by their guids.
  If an error occurs during the fetch, throw the error.
  If the requests timeout, throw an exception about timing out.
  Return the collected values of async fetches to the api
  combined into a single normalized results collection"
  [api-key game-ids]
  (let [err-chan (chan)]
    (->> game-ids
         (map (partial game-by-id api-key err-chan))
         (async-utils/all err-chan)
         (into [] normalize-results-xf))))

(defn query-games [api-key game-name]
  (->> game-name
       (query-endpoint api-key)
       parsed-results
       (into [] normalize-results-xf)))
