(ns todomvc.frontend.views
  (:require
   [re-frame.core :as re-frame]
   ["react-dom/client" :refer [createRoot]]))

(defn component []
  (let [match @(re-frame/subscribe [:current-route])]
    [:div
     (if match
       (let [view (:view (:data match))]
         [view match]))]))

(defn home-page []
  [:div
   [:h2 "Home page"]])

(defonce root (createRoot (.getElementById js/document "app")))
