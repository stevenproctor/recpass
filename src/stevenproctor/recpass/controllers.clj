(ns stevenproctor.recpass.controllers
  (:require [stevenproctor.recpass.views :as views]))

(defn checkout [{{:keys [game-ids]} :params}]
  (views/checkout game-ids))

(defn search-games [query-games]
  (fn  [{{:keys [q]} :params}]
    (-> q
        query-games
        views/search-results)))

(defn home [_req]
  (views/home))

(defn not-found [_req]
  (views/not-found))
