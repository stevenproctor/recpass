(ns stevenproctor.recpass.giantbomb
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(defn query-endpoint [api-key game-name]
  (http/get
   "https://www.giantbomb.com/api/games/"
   {:headers {:user-agent "RecPass breakable-toy programming problem client for testing library usage"}
    :query-params {:api_key api-key
                   :format "json"
                   :limit 10
                   :field_list "id,image,name"
                   :sort "name:asc"
                   :filter (str "name:" game-name)}}))

(System/getenv "GIANT_BOMB_API_KEY")

(defn query-games [api-key game-name]
  (->
   (query-endpoint api-key game-name)
   :body
   (json/parse-string true)
   :results
   ((partial map
             #(merge (select-keys % [:id :name])
                     {:image-url (get-in % [:image :small_url])
                      :image-icon-url (get-in % [:image :icon_url])})))))
