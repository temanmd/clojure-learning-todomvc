(ns todomvc.frontend.events-test
  (:require [cljs.test :refer-macros [deftest
                                      is
                                      testing
                                      use-fixtures]]
            [reagent.dom :as rdom]
            [re-frame.core :refer [dispatch-sync subscribe]]
            [todomvc.frontend.events]
            [todomvc.frontend.subs]
            [todomvc.frontend.effects]))

(defn initialize-app-db
  [f]
  (reset! re-frame.db/app-db {})
  (dispatch-sync [:initialize])
  (f))

(use-fixtures :each initialize-app-db)

(deftest events-test

  (testing "[:initialize] event"
    (is (= @re-frame.db/app-db
           {:todos {} :showing :all})))

  (testing "[:set-showing] event"
    (let [showing (subscribe [:showing])]
      (is (= @showing :all))
      (dispatch-sync [:set-showing :done])
      (is (= @showing :done)))) 
  
  (testing "[:add-todo] event"
    (let [todos (subscribe [:todos])]
      (is (= @todos {}))
      (dispatch-sync [:add-todo "Make a breakfast"])
      (is (=
           (-> @todos
               first)
           {:id 1 :title "Make a breakfast" :done false})))))