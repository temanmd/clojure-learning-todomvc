(ns todomvc.frontend.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx]]))

;; Initialize DB

(reg-event-db
 :initialize
 (fn [__]
   {:current-route nil}))

;; Routes

(reg-event-fx
 :navigate
 (fn [db [_ route]]
   {:navigate! route}))

(reg-event-db
 :navigated
 (fn [db [_ new-match]]
   (assoc db :current-route new-match)))
