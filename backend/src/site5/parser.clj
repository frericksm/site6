(ns site5.parser
  (:require [datomic.api :as d]
            [site5.db.util :as db-util]
            [om.next.server :as om]))

;;;;;;;;;;;
;; Reads ;;
;;;;;;;;;;;

(defmulti readf (fn [env k params] k))

(defn todos
  "Pull todos from database"
  ([db] (todos db '[*]))
  ([db query]
   (d/q '[:find [(pull ?eid query) ...]
          :in $ query
          :where [?eid :todo/text]]
     db query)))

(defmethod readf :todo/list
  [{:keys [conn query]} _ _]
  {:value (todos (d/db conn) query)})

;;;;;;;;;;;;;;;
;; Mutations ;;
;;;;;;;;;;;;;;;

(defmulti mutate (fn [env k params] k))

(defmethod mutate 'todo/create
  [{:keys [conn]} _ params]
  {:value {:keys [:todo/list]}
   :action
   (fn []
     (let [client-id (:db/id params)
           temp-id #db/id[:db.part/user -100]
           result @(d/transact conn
                     [(merge params {:db/id temp-id
                                     :todo/text "new todo"})])
           new-id (db-util/resolve-tempid result temp-id)]
       {:tempids {[:todo/by-id client-id] [:todo/by-id new-id]}}))})

(defmethod mutate 'todo/delete
  [{:keys [conn]} _ {:keys [db/id]}]
  {:value {:keys [:todo/list]}
   :action
   (fn []
     (d/transact conn [[:db.fn/retractEntity id]])
     {})})

(defmethod mutate 'todo/update
  [{:keys [conn]} _ {:keys [db/id] :as todo-obj}]
  {:value {:keys [[:todo/by-id id]]}
   :action
   (fn []
     (d/transact conn [todo-obj])
     nil)})

(def parser (om/parser {:read readf :mutate mutate}))
