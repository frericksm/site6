(ns site6.eventbus
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [com.stuartsierra.component :as component]
            [cljs.core.async :as async
             :refer [>! <! put! chan alts! close!]]))

(defrecord Eventbus []
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting Eventbus") 
    (let [c (chan)] 
      (assoc this :channel c)))

  (stop [this]
    (println "Stopping Eventbus")
    (close! (:channel this))
    (dissoc this :channel)))


(defn new-eventbus []
  (map->Eventbus {}))
