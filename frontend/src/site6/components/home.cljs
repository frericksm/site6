(ns site6.components.home
  (:require [cljsjs.material-ui]
            
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            
            [cljs-react-material-ui.icons :as ic]
            [goog.dom :as gdom]

            [site6.route]

            [reagent.core :as reagent]
            [re-frame.core :as rf]

   
            [site6.components.notes]

            [secretary.core :as secretary :refer-macros [defroute]]

            [com.stuartsierra.component :as component]
            [cljs.core.async :as async :refer [>! <! put! chan alts! close!]]))

(def path "/")
(def route :index)

#_(defui Home
  static om/IQuery
  (query [this]
    [{:note/list (om/get-query site6.components.notes/NoteListEntry)}])
  Object
  (render [this]))

(defn home []
  "Home!!!!!")

(defn add-route [] (rf/dispatch [:route-change route]))

(defrecord HomeComp [eventbus route-manager]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting HomeComp") 
    (let [ebch (:channel eventbus)]
      (secretary/add-route! path add-route)
      (site6.route/add-route route-manager route home)
      this))


  (stop [this]
    (println "Stopping HomeComp")
    this))

(defn new-home []
  (map->HomeComp {}))






