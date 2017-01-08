(ns site5.system 
  (:require  [com.stuartsierra.component :as component]
             [site5.ui]
             [site5.state2]
             [site5.route]
             [site5.components.home]
             [site5.components.notes]
             [site5.eventbus]))

(defn system [config-options]
  (component/system-map
   :state (site5.state2/new-state)
   :eventbus (site5.eventbus/new-eventbus)
   :route-manager (component/using 
                   (site5.route/new-route-manager)
                   [:eventbus])
   :home (component/using 
          (site5.components.home/new-home)
          [:eventbus])
   :notes (component/using 
           (site5.components.notes/new-notes)
           [:eventbus])
   :ui    (component/using
           (site5.ui/new-ui)
           [:state :eventbus :home :notes :route-manager])))
