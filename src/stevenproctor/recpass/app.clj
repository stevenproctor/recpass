(ns stevenproctor.recpass.app
  (:require
   [bidi.bidi :refer [match-route]]
   [bidi.ring :refer [make-handler]]
   [clojure.walk :refer [keywordize-keys]]
   [hiccup.form :refer [form-to submit-button text-field check-box]]
   [hiccup.page :refer [html5]]
   [ring.middleware.flash :refer [wrap-flash]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.session :refer [wrap-session]]
   [ring.util.response :as res]))

(defn recpass-page [& body]
  (res/response
   (html5
    [:body
     [:div
      [:h1  "RecPass - Your Video Game Rental Service"]
      [:div {:id :menu}
       [:a {:href "/"} "Home"]]
      body]])))

(defn checkout [{{:keys [games] :as params} :params}]
  (println games)
  (println params)
  (recpass-page [:p "Checkout"]))

(defn search-game [{{:keys [q]} :params}]
  (recpass-page [:div
                 [:p q]
                 (form-to [:post "checkout"]
                          [:div
                           [:label {:for :search-game-name} "Game Name"]
                           (check-box {:id :search-game-name :value 1} :game)

                           [:label {:for :search-game-name2} "Game Name"]
                           (check-box {:id :search-game-name :value 2} :game)]
                          (submit-button "Checkout"))]))

(defn home [_req]
  (recpass-page
   (form-to [:post "search"]
            [:div
             [:label {:for :search-game-name} "Game Name"]
             (text-field {:id :search-game-name} :q)]
            (submit-button "Search"))))

(defn not-found [_req]
  (recpass-page
   [:div {:id "404"}
    "The page you requested could not be found"]))

(def routes
  ["/" {"" home
        {:request-method :post} {"search" search-game
                                 "checkout" checkout}
        true not-found}])

(letfn [(keywordize-request-key [request k]
          (if (k request)
            (update request k keywordize-keys)
            request))
        (keywordize-params [request]
          (-> request
              (keywordize-request-key :form-params)
              (keywordize-request-key :query-params)
              (keywordize-request-key :params)))]
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

(comment
  (search-game "zelda")

  (def route ["/index.html" home])
  (match-route routes "/search" :request-method :post)
  (match-route routes "/search"))
