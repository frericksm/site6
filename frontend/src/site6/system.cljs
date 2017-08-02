(ns site6.system 
  (:require  [com.stuartsierra.component :as component]
             [site6.ui]
             [site6.state]
             [site6.route]
             [site6.components.app]
             [site6.components.home]
             [site6.components.notes]
             [site6.components.note-editor]
             [site6.eventbus]))

(defn system [config-options]
  (component/system-map
   :application (component/using (site6.components.app/new-app)
                                 [:route-manager])
   :state (site6.state/new-state)
   :eventbus (site6.eventbus/new-eventbus)
   :route-manager (component/using 
                   (site6.route/new-route-manager)
                   [:eventbus])
   :home (component/using 
          (site6.components.home/new-home)
          [:eventbus :route-manager])
   :notes (component/using 
           (site6.components.notes/new-notes)
           [:eventbus :route-manager])
   :note-editor (component/using 
                 (site6.components.note-editor/new-note-editor)
                 [:eventbus :route-manager])
   :ui    (component/using
           (site6.ui/new-ui)
           [:application :state :eventbus :home :notes :note-editor :route-manager])))
