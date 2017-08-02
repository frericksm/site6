(ns site6.parser
  (:require [om.next :as om]
            [site6.state2 :refer [init-state]]))

;; =============================================================================
;; Reads

(defmulti read om/dispatch)

;;;;;;;;;;;;;;;;;;;;;; start: Read for routes

#_(defn read-route [state query k]
  (let [st @state
        v (om/db->tree query st st)]
    #_(println ":notes read k " k )
    #_(println ":notes read query"  query )
    #_(println ":notes read state " @state)
    #_(println ":notes read value " v)
    {:remote false
     :value  v}))

#_(defmethod read :notes
  [{:keys [state query]} k _]
  (read-route state query k))

#_(defmethod read :index
  [{:keys [state query]} k _]
  (read-route state query k))

#_(defmethod read :note-editor
  [{:keys [state query]} k _]
  (read-route state query k))

;;;;;;;;;;;;;;;;;;;;;;;; end: Read for routes 

(defmethod read :note/current
  [{:keys [state query]} k _]
  #_(println ":note/current read " k query ", state: " @state)
  {:value (k @state)})

(defmethod read :person/new
  [{:keys [state]} k _]
  {:value (k @state)})

(defmethod read :default
  [{:keys [state query]} k _]
  #_(println "Default read " k query ", state: " @state)
  (let [st @state]
    {:remote false
     :value  (om/db->tree query (k st) st)}))

(defmulti mutate om/dispatch)

(defmethod mutate :default
  [_ k _]
  (println "Default mutate " k)
  {:remote false})

(defmethod mutate 'note/current
  [{:keys [state] :as env} key {:keys [db/id]}]
  {:action (fn []
             #_(println "value: " id )
             (let [cn (get-in @state [:note/by-id id])]
               #_(println "cn: " cn) 
               (swap! state assoc :note/current cn)))})

(defmethod mutate 'note-current/change
  [{:keys [state]} _ {:keys [value path]}]
  {:action (fn []
             (swap! state assoc-in (cons :note/current path) value))})

(defmethod mutate 'person-new/change
  [{:keys [state]} _ {:keys [value path]}]
  {:action (fn []
             (swap! state assoc-in (cons :person/new path) value))})


(defmethod mutate 'person-new/add
  [{:keys [state]} _]
  {:action (fn []
             (let [id (rand-int 9999)
                   person-new (-> (:person/new @state)
                                  (assoc :db/id id))]
               (swap! state assoc-in [:person/by-id id] person-new)
               (swap! state update :person/list conj [:person/by-id id])
               (swap! state assoc :person/new (:person/new init-state))))})
