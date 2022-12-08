(ns todomvc.frontend.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx path after]]
   [todomvc.frontend.db :refer [default-db todos->local-store]]
   [cljs.spec.alpha :as s]))

;;; Interceptors --------------------------------------

(defn check-and-throw
  "Throws an exception if db does not match the Spec a-spec"
  [a-spec db]
  (println db)
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "Spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :todomvc.frontend.db/db)))

(def ->local-store (after todos->local-store))

(def todo-interceptors [check-spec-interceptor
                        (path :todos)
                        ->local-store])

;;; Helpers -------------------------------------------

(defn allocate-next-id
  "Returns the next todo id.
  Assumes todos are sorted.
  Returns one more than the current largest id."
  [todos]
  ((fnil inc 0) (last (keys todos))))

;;; Events --------------------------------------------

;; Initialize DB

(reg-event-fx
 :initialize

 [(inject-cofx :local-store-todos)
  check-spec-interceptor]

 (fn [{:keys [db local-store-todos]} _]
   {:db (assoc default-db :todos local-store-todos)}))

;; Todos

(reg-event-db
 :add-todo

 todo-interceptors

 (fn [todos [_ text]]
   (let [id (allocate-next-id todos)]
     (assoc todos id {:id id :title text :done false}))))

;; Routes

(reg-event-fx
 :navigate
 (fn [db [_ route]]
   {:navigate! route}))

(reg-event-db
 :navigated
 (fn [db [_ new-match]]
   (assoc db :current-route new-match)))
