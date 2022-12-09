(ns todomvc.frontend.views
  (:require
   [re-frame.core :refer [dispatch subscribe]]
   [reagent.core :as reagent]
   [clojure.string :as str]
   ["react-dom/client" :refer [createRoot]]))

(defn component
  []
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

(defn todo-item
  []
  (let [editing (reagent/atom false)]
    (fn [{:keys [id done title]}]
      [:li {:class (str (when done "completed")
                        (when @editing "editing"))}
       [:div.view
        [:input.toggle
         {:type "checkbox"
          :checked done
          :on-change #(dispatch [:toggle-done id])}]
        [:label
         {:on-double-click #(reset! editing true)}
         title]
        [:button.badge.text-bg-danger.ms-2
         {:on-click #(dispatch [:delete-todo id])
          :type "button"}
         "Delete"]]
       (when @editing
         [todo-input
          {:class "edit"
           :title title
           :on-save #(if (seq %)
                       (dispatch [:save id %])
                       (dispatch [:delete-todo id]))
           :on-stop #(reset! editing false)}])])))

(defn task-list
  []
  (let [visible-todos @(subscribe [:visible-todos])
        all-complete? @(subscribe [:all-complete?])]
    [:section#main
     [:input#toggle-all
      {:type "checkbox"
       :checked all-complete?
       :on-change #(dispatch [:complete-all-toggle])}]
     [:label
      {:for "toggle-all"}
      "Mark all as complete"]
     [:ul#todo-list.mt-3
      (for [todo visible-todos]
        ^{:key (:id todo)} [todo-item todo])]]))

(defn home-page
  []
  [:div
   [:h1 "Home page"]
   [task-entry]
   (when (seq @(subscribe [:todos]))
     [task-list])])

(defonce root (createRoot (.getElementById js/document "app")))
