(ns stevenproctor.recpass.views
  (:require
   [hiccup.form :refer [form-to submit-button text-field check-box]]
   [hiccup.page :refer [html5 include-css]]
   [ring.util.response :as res]))

(defn recpass-page [& body]
  (res/response
   (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
     [:meta {:name "viewport" :content
             "width=device-width, initial-scale=1, maximum-scale=1"}]
     (include-css "/stylesheets/screen.css")]
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
    [:div {:class "result"}
     [:img {:src (:image-url item) :class "cover-art" :alt (str (:name item) " cover art")}]
     [:div
      (check-box {:id checkbox-id :value (:id item)} :game-ids)
      [:label {:for checkbox-id} (:name item)]]]))

(defn search-results [results]
  (recpass-page [:div
                 (form-to [:post "checkout"]
                          [:div {:class "results"}
                           (map-indexed show-game results)]
                          (submit-button {:class "primary-action"} "Checkout"))]))

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

