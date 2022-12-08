(ns todomvc.frontend.views
  (:require
   [re-frame.core :refer [dispatch subscribe]]
   [reagent.core :as reagent]
   [clojure.string :as str]
   ["react-dom/client" :refer [createRoot]]))

(defn component []
  (let [match @(subscribe [:current-route])]
    [:div
     (if match
       (let [view (:view (:data match))]
         [view match]))]))

(defn todo-input
  [{:keys [title on-save on-stop]}]
  (let [val (reagent/atom title)
        stop #(do (reset! val "")
                  (when on-stop (on-stop)))
        save #(let [v (-> @val str str/trim)]
                (on-save v)
                (stop))]
    (fn [props]
      [:input.form-control (merge (dissoc props :on-save :on-stop :title)
                     {:type "text"
                      :value @val
                      :auto-focus true
                      :on-blur save
                      :on-change #(reset! val (-> % .-target .-value))
                      :on-key-down #(case (.-which %)
                                      13 (save)
                                      27 (stop)
                                      nil)})])))

(defn task-entry
  []
  [:<>
   [:h2 "Todos"]
   [todo-input
    {:id "new-todo"
     :placeholder "What needs to be done?"
     :on-save #(when (seq %)
                 (dispatch [:add-todo %]))}]])

(defn task-list
  []
  (let [visible-todos @(subscribe [:todos])]
    [:ul#todo-list.mt-3
     (for [todo visible-todos]
       ^{:key (:id todo)} [:li (:title todo)
                           [:button.badge.text-bg-danger.ms-2
                            {:on-click #(dispatch [:delete-todo (:id todo)])
                             :type "button"}
                            "x"]])]))

(defn home-page []
  [:div
   [:h1 "Home page"]
   [task-entry]
   [task-list]])

(defonce root (createRoot (.getElementById js/document "app")))