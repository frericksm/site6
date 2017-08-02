(ns site6.components.app
  (:require [com.stuartsierra.component :as component]

            [cljsjs.material-ui]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]
            [re-frame.core :as rf]
            [reagent.core :as reagent]
          ))





(defn change-drawer-state [new-value]
  (rf/dispatch [:drawer-change new-value]))


(defn app-wrapper-factory [route-manager]
  (fn []
    (let [active-route @(rf/subscribe [:route])
          r2rf-atom (get route-manager :route-to-render-fn)
          r2rf (deref r2rf-atom)
          body (get r2rf active-route)
          drawer-open? @(rf/subscribe [:drawer-open?]) 
          close-help (fn [e] #_(om/update-state! this assoc :open-help? false))]
      [ui/mui-theme-provider {:mui-theme (get-mui-theme)}
       [:div {:className "h-100"}
        [ui/app-bar
         {:title "Dashboard"
          :show-menu-icon-button true
          ;;:icon-element-left (ui/icon-button (ic/navigation-close))
          :icon-element-right  (reagent/as-element [ui/flat-button
                                                    {:label     "Home"
                                                     :href      "https://tipevi.de/home"
                                                     :secondary true}])
          :on-left-icon-button-touch-tap
          (fn [e] (change-drawer-state true))}]
        [ui/drawer
         {:docked            false
          :open              @(rf/subscribe [:drawer-open?])
          :on-request-change (fn [] (change-drawer-state (not drawer-open?)))}
         [ui/menu-item {:on-touch-tap #(do (change-drawer-state false)
                                           (site6.route/app-dispatch :index))} "Home"]
         [ui/menu-item {:on-touch-tap #(do (change-drawer-state false)
                                           (site6.route/app-dispatch :notes))} "Notes"]]

        (if (not (nil? body)) (body))
        [ui/dialog
         {:title            "Help"
          :key              "dialog"
          :modal            false
          :open             false #_(boolean (:open-help? state))
          :actions          [(reagent/as-element [ui/raised-button
                                                  {:label        "Back"
                                                   :key          "back"
                                                   :on-touch-tap close-help}])
                             (reagent/as-element [ui/raised-button
                                                  {:label        "Thanks"
                                                   :key          "thanks"
                                                   :on-touch-tap close-help}])]
          :on-request-close close-help}]
        ]])))


(defrecord Application [route-manager]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting Application") 
    (assoc this :application-render-fn (app-wrapper-factory route-manager)))

  (stop [this]
    (println "Stopping Application")
    (as-> this x
      (dissoc x :application-render-fn))))


(defn new-app []
  (map->Application {}))
