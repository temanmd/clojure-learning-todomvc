(ns todomvc.frontend.subs
  (:require
   [re-frame.core :refer [reg-sub]]))

;; Routes

(reg-sub
 :current-route
 (fn [db]
   (:current-route db)))
