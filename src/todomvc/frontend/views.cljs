(ns todomvc.frontend.views
  (:require
   ["react-dom/client" :refer [createRoot]]))

(defn component []
  [:div
   [:h1 "Hello, World and Clojurians!"]
   [:h3 "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red"]
    " text."]])

(defn home-page []
  [:div
   [:h2 "Home page"]])

(defonce root (createRoot (.getElementById js/document "app")))
