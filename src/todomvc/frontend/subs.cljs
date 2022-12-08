(ns todomvc.frontend.subs
  (:require
   [re-frame.core :refer [reg-sub subscribe]]))

;; Routes

(reg-sub
 :current-route
 (fn [db]
   (:current-route db)))

;; Todos

(reg-sub
  :sorted-todos
  (fn [db _]
    (:todos db)))

(reg-sub
  :todos
  (fn [query-v _]
    (subscribe [:sorted-todos]))
  (fn [sorted-todos query-v _]
    (vals sorted-todos)))
