(ns site5.state
  (:require [om.next :as om]
            [site5.util :as util]))

(defonce app-state
  ;; {:todo/list [[:todo/by-id 1] [:todo/by-id 2]]
  ;;  :todo/by-id {1 {:db/id 1 :todo/text "First Todo"}
  ;;               2 {:db/id 2 :todo/text "Second Todo"}}}
  (atom {}))

(defn ^:export app-state-to-js
  "Inspect the current app state in the console"
  []
  (prn @app-state))

;;;;;;;;;;
;; Read ;;
;;;;;;;;;;

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state]} k params]
  (let [st @state]
    (if (contains? st k)
      {:value (get st k)}
      {:remote true})))

(defmethod read :todo/list
  [{:keys [state query]} key _]
  (let [st @state]
    (if (contains? st key)
      {:value (om/db->tree query (get st key) st)}
      ;; First pull
      {:remote true})))

;;;;;;;;;;;;
;; Mutate ;;
;;;;;;;;;;;;

(defmulti mutate om/dispatch)

(defn create-todo [state params]
  (let [id (:db/id params)
        ref [:todo/by-id id]]
    (-> state
      (assoc-in ref {:db/id id :todo/text "new todo"})
      (update :todo/list conj ref))))

(defmethod mutate 'todo/create
  [{:keys [state]} _ params]
  {:action (fn [] (swap! state create-todo params))
   :remote true})

(defn delete-todo [state id]
  (let [ref [:todo/by-id id]]
    (-> state
      (util/dissoc-in ref)
      (update :todo/list #(util/remove-from-vector % ref)))))

(defmethod mutate 'todo/delete
  [{:keys [state]} _ {:keys [db/id]}]
  {:action (fn [] (swap! state delete-todo id))
   :remote true})

(defn update-todo [state {:keys [db/id todo/text]}]
  (assoc-in state [:todo/by-id id :todo/text] text))

(defmethod mutate 'todo/update
  [{:keys [state]} _ opts]
  {:action (fn [] (swap! state update-todo opts))
   :remote true})

(def parser (om/parser {:read read :mutate mutate}))
