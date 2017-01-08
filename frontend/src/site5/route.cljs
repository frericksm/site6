(ns site5.route
  (:require [com.stuartsierra.component :as component]

            [secretary.core :as secretary :refer-macros [defroute]]
            [goog.history.EventType :as EventType]
            [goog.events :as evt]
            [compassus.core :as compassus]
            )
  (:import goog.History))

(def routing-data 
  {:index "/"
   :notes "/notes"})

(defn app-dispatch [route]
  ;; used for deployment under /
  ;;(.assign js/location (str "/#" (get routing-data route)))
  (.assign js/location (str "#" (get routing-data route))))

(defn did-mount [route-manager] 
  (let [event-key (:event-key route-manager)
        history (:history route-manager)]
    (compassus/did-mount 
     (fn [_]
       (reset! event-key
               (evt/listen history EventType/NAVIGATE
                           (fn [e]
                             (secretary/dispatch! (.-token e)))))
       (.setEnabled history true)))))

(defn will-unmount [route-manager]
  (let [event-key (:event-key route-manager)]
    (compassus/will-unmount 
     (fn  [_] (evt/unlistenByKey @event-key)))))


(defn register-route [route-manager path route ui-comp]
)

(defrecord RouteManager [eventbus]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting RouteManager") 
    (let [event-key (atom nil)
          history (History.)] 
      (as-> this x 
        (assoc x :event-key event-key)
        (assoc x :history history))))

  (stop [this]
    (println "Stopping RouteManager")
    (as-> this x
      (dissoc x :event-key)
      (dissoc x :history))))


(defn new-route-manager []
  (map->RouteManager {}))
