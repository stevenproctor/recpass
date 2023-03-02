(ns stevenproctor.recpass.app
  (:require
   [bidi.ring :refer [make-handler resources resources-maybe]]
   [clojure.walk :refer [keywordize-keys]]
   [ring.middleware.flash :refer [wrap-flash]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.session :refer [wrap-session]]
   [stevenproctor.recpass.controllers :as controllers]
   [stevenproctor.recpass.giantbomb :as giantbomb]))

(def api-key (System/getenv "GIANT_BOMB_API_KEY"))

(def query-games
  (partial giantbomb/query-games api-key))

(def routes
  ["/" {"" controllers/home
        {:request-method :post} {"search" (controllers/search-games query-games)
                                 "checkout" controllers/checkout}
        "stylesheets" (resources-maybe {:prefix "public/stylesheets"})
        true controllers/not-found}])

(letfn [(keywordize-request-key [request k]
          (if (k request)
            (update request k keywordize-keys)
            request))
        (keywordize-params [request]
          (reduce #(keywordize-request-key %1 %2)
                  request
                  [:form-params
                   :query-params
                   :params]))]
  (defn wrap-keywordize-params
    ([handler]
     (fn
       ([request]
        (handler (keywordize-params request)))
       ([request respond raise]
        (handler (keywordize-params request) respond raise))))))

(def handler
  (make-handler routes))

(def app (-> handler
             wrap-keywordize-params
             wrap-params
             wrap-session
             wrap-flash))
