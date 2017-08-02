(ns site6.route
  (:require [com.stuartsierra.component :as component]

            [secretary.core :as secretary :refer-macros [defroute]]
            [goog.history.EventType :as EventType]
            [goog.events :as evt]
            )
  (:import goog.History))

(def routing-data 
  {:index "/"
   :notes "/notes"
   :note-editor "/note-editor"})

(defn add-route [route-manager route render-fn]
  (swap! (get route-manager :route-to-render-fn)
         (fn [current-value] (assoc current-value route render-fn))))


(defn app-dispatch [route]
  ;; used for deployment under /
  ;;(.assign js/location (str "/#" (get routing-data route)))
  (println "app-dispatch: " route)
  (let [loc js/location]
    (println "loc: " loc)
    (.assign loc (str "#" (get routing-data route)))))


(defn did-mount [route-manager] 
  (let [event-key (:event-key route-manager)
        history (:history route-manager)]
    (reset! event-key
            (evt/listen history EventType/NAVIGATE
                        (fn [e]
                          (secretary/dispatch! (.-token e)))))
    (.setEnabled history true)))

(defn will-unmount [route-manager]
  (let [event-key (:event-key route-manager)]
    (evt/unlistenByKey @event-key)))

(defrecord RouteManager [eventbus]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting RouteManager") 
    (let [event-key (atom nil)
          history (History.)
          route-to-render-fn (atom nil)] 
      (as-> this x 
        (assoc x :event-key event-key)
        (assoc x :history history)
        (assoc x :route-to-render-fn route-to-render-fn)
        (do (did-mount x) x))))

  (stop [this]
    (println "Stopping RouteManager")
    (as-> this x
      (dissoc x :event-key)
      (dissoc x :history)
      (dissoc x :route-to-render-fn)
      (do (will-unmount x) x))))


(defn new-route-manager []
  (map->RouteManager {}))
