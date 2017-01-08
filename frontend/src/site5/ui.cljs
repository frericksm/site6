(ns site5.ui
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [com.stuartsierra.component :as component]

            [cljs.core.async :as async
             :refer [>! <! put! chan alts! close!]]

            [om.next :as om]
            [goog.dom :as gdom]
            [site5.parser :as p]

            [site5.components.app]
            [compassus.core :as compassus]
            [site5.route]
            ))


(defn print-and-return [lable o]
  (println lable o)
  o)

(defn set-up-navigation [app ch]
  (go (while true
        (let [[f v] (<! ch)]
          (if (= f 'set-route!)
            (compassus/set-route! app v))))))

(defrecord UserInterface [state eventbus home notes route-manager]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting UI") 
    (let [ebch (:channel eventbus) 
          history (:history route-manager)
          config {:routes (merge (:routes home)
                                 (:routes notes)) 
                  :index-route :index
                  :reconciler (om/reconciler
                               {:state (:app-state state)
                                :normalize true
                                :parser (compassus/parser {:read p/read :mutate p/mutate} )})
                  :mixins [(compassus/wrap-render site5.components.app/app-wrapper) 
                           (site5.route/did-mount route-manager)
                           (site5.route/will-unmount route-manager)]}
          app (compassus/application config)]
      (set-up-navigation app ebch)
      (compassus/mount! app (gdom/getElement "app"))
      (assoc this :app app)))


  (stop [this]
    (println "Stopping UI")
    (dissoc this :app)))


(defn new-ui []
  (map->UserInterface {}))
