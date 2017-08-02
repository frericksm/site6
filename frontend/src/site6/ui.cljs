(ns site6.ui
  (:require [com.stuartsierra.component :as component]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
            [goog.dom :as gdom]
            [site6.components.app]
            [site6.components.home]
            ))

(defn init-queries [ui]
  (rf/reg-sub
   :drawer-open?
   (fn [db _]     ;; db is current app state. 2nd unused param is query vector
     (-> db
         :drawer-open?)))
  (rf/reg-sub
   :route
   (fn [db _]     ;; db is current app state. 2nd unused param is query vector
     (-> db
         :route))))

(defn init-event-handlers [ui]
  (rf/reg-event-db        
   :drawer-change         
   (fn [db [_ new-value]] (assoc db :drawer-open? new-value)))

  (rf/reg-event-db        
   :route-change         
   (fn [db [_ new-value]]
     #_(println new-value)
     (assoc db :route new-value))))

(defrecord UserInterface [application state eventbus home notes note-editor route-manager]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting UI") 
    (let [r-fn (get application :application-render-fn)]
      (init-queries this)
      (init-event-handlers this)      
      (rf/dispatch-sync [:initialize])  ;; puts a value into application state
      (reagent/render [r-fn]
                      (gdom/getElement "app")) 
      (rf/dispatch [:route-change :index])
      this))

  (stop [this]
    (println "Stopping UI")
    this))


(defn new-ui []
  (map->UserInterface {}))


