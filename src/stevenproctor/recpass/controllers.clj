(ns stevenproctor.recpass.controllers
  (:require [stevenproctor.recpass.views :as views]))

(defn home [_req]
  (views/home))

(defn not-found [_req]
  (views/not-found))

(defn checkout [{{:keys [_game-ids]} :params}]
  (views/checkout))

(defn show-cart [games-by-ids]
  (fn  [{{:keys [game-ids]} :params}]
    (try
      (-> game-ids
          games-by-ids
          views/show-cart)
      (catch Throwable e
        (println e)
        (views/oops)))))

(defn search-games [query-games]
  (fn  [{{:keys [q]} :params}]
    (try
      (-> q
          query-games
          views/search-results)
      (catch Throwable e
        (println e)
        (views/oops)))))
