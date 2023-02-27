(ns stevenproctor.recpass.views
  (:require
   [hiccup.form :refer [form-to submit-button text-field check-box]]
   [hiccup.page :refer [html5]]
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

(defn checkout [ids]
  (recpass-page [:p "Checkout"]))

(defn show-game [idx item]
  (let [checkbox-id (str "search-game-name" idx)]
    [:div
     [:img {:src (:image-url item) :alt (str (:name item) " cover art")}]
     [:div
      [:label {:for checkbox-id} (:name item)]
      (check-box {:id checkbox-id :value (:id item)} :game-ids)]]))

(defn search-results [results]
  (recpass-page [:div
                 (form-to [:post "checkout"]
                          [:div
                           (map-indexed show-game results)]
                          (submit-button "Checkout"))]))

(defn home []
  (recpass-page
   (form-to [:post "search"]
            [:div
             [:label {:for :search-game-name} "Game Name"]
             (text-field {:id :search-game-name} :q)]
            (submit-button "Search"))))

(defn not-found []
  (recpass-page
   [:div {:id "404"}
    "The page you requested could not be found"]))

