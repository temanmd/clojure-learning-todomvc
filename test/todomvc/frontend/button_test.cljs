(ns todomvc.frontend.button-test
  (:require [cljs.test :refer-macros [deftest is testing use-fixtures]]
            [reagent.dom :as rdom]))

(defn button [{:keys [on-click text disabled]}]
  [:button
   {:type "button"
    :disabled disabled
    :on-click #(on-click)}
   text])

(defn create-app-element [f]
  (.appendChild (.-body js/document)
                (doto (.createElement js/document "div")
                  (-> (.setAttribute "id" "app"))
                  (-> (.setAttribute "style" "display:none;"))))
  (f))

(use-fixtures :once create-app-element)

(deftest button-component-test
  (testing "Renders correctly"
    (rdom/render [button {:on-click #(println "hi")
                          :text "button"
                          :disabled false}]
                 (.getElementById js/document "app"))
    (is (= true true))))
