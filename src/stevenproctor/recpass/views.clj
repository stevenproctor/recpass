(ns stevenproctor.recpass.views
  (:require
   [hiccup.form :refer [form-to submit-button text-field check-box hidden-field]]
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

(defn show-cart-item [idx item]
  (let [cart-item-id (str "cart-item-" idx)]
    [:div {:class "cart-item"}
     [:img {:src (:image-icon-url item) :class "thumbnail" :alt (str (:name item) " cover art thumbnail")}]
     [:label {:for cart-item-id} (:name item)]
     (hidden-field {:id cart-item-id :value (:guid item)} :game-ids)]))

(defn show-game [idx item]
  (let [checkbox-id (str "search-game-name" idx)]
    [:div {:class "result"}
     [:img {:src (:image-url item) :class "cover-art" :alt (str (:name item) " cover art")}]
     [:div
      (check-box {:id checkbox-id :value (:guid item)} :game-ids)
      [:label {:for checkbox-id} (:name item)]]]))

(defn show-cart [cart-items]
  (recpass-page
   [:div
    [:h3 "My Cart"]
    [:div
     (form-to [:post "checkout"]
              [:div {:class "cart-items"}
               (map-indexed show-cart-item cart-items)]
              (submit-button {:class "primary-action"} "Rent Games"))]]))

(defn checkout []
  (recpass-page
   [:div
    [:h3 "Thank you for your rental"]
    [:a {:href "/"} "Continue Browsing Games"]]))

(defn search-results [results]
  (recpass-page [:div
                 (form-to [:post "cart"]
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

(defn oops []
  (recpass-page
   [:div {:id "500"}
    [:div {:class "oops"}
     [:h2 "Something went wrong"]
     [:span {:class "sad-panda"} "😢🐼"]
     [:p "We apologize for the inconvenience and have automatically reported the error to our team."]]]))
