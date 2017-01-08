(ns site5.parser
  (:require [om.next :as om]
            [site5.state2 :refer [init-state]]))

;; =============================================================================
;; Reads

(defmulti read om/dispatch)

;;;;;;;;;;;;;;;;;;;;;; start: Read for routes

(defmethod read :notes
  [{:keys [state query]} k _]
  ;(println ":notes read " k query)
  (let [st @state]
    {:remote false
     :value  (om/db->tree query st st)}))

(defmethod read :index
  [{:keys [state query]} k _]
  ;(println ":index read " k query)
  (let [st @state]
    {:remote false
     :value  (om/db->tree query st st)}))

;;;;;;;;;;;;;;;;;;;;;;;; end: Read for routes 


(defmethod read :default
  [{:keys [state query]} k _]
  ;(println "Default read " k query)
  (let [st @state]
    {:remote false
     :value  (om/db->tree query (k st) st)}))

(defmethod read :person/new
  [{:keys [state]} k _]
  {:value (k @state)})

(defmulti mutate om/dispatch)

(defmethod mutate :default
  [_ k _]
  (println "Default mutate " k)
  {:remote false})

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
