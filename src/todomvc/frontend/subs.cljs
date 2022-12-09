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
  :showing
  (fn [db _]
    (:showing db)))

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

(reg-sub
  :visible-todos
  (fn [query-v _]
    [(subscribe [:todos])
     (subscribe [:showing])])
  (fn [[todos showing] _]
    (let [filter-fn (case showing
                      :all identity
                      :done :done
                      :active (complement :done))]
      (filter filter-fn todos))))

(reg-sub
  :all-complete?
  (fn [_ _]
    (subscribe [:todos]))
  (fn [todos _]
    (every? :done todos)))

