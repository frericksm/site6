(ns site5.components.home
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :as ui]
            [cljs-react-material-ui.icons :as ic]
            [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]

            [site5.components.notes]

            [secretary.core :as secretary :refer-macros [defroute]]

            [com.stuartsierra.component :as component]
            [cljs.core.async :as async :refer [>! <! put! chan alts! close!]]))

(def path "/")
(def route :index)

(defui Home
  static om/IQuery
  (query [this]
    [{:note/list (om/get-query site5.components.notes/NoteListEntry)}])
  Object
  (render [this]))


(defrecord HomeComp [eventbus]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting HomeComp") 
    (let [ebch (:channel eventbus)]
      (secretary/add-route! path (fn [] (put! ebch ['set-route! route])))
      (assoc-in this [:routes route]  Home)))


  (stop [this]
    (println "Stopping HomeComp")
    (assoc-in this [:routes route] nil)))

(defn new-home []
  (map->HomeComp {}))






