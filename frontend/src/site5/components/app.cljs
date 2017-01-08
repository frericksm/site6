(ns site5.components.app
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :as ui]
            [cljs-react-material-ui.icons :as ic]
            [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [site5.route]))

(defn change-drawer-state [this state]
  (om/set-state! this {:drawer-open? state}))

(defui App 
  Object
  (render [this]
          (let [{:keys [owner factory props]} (om/props this)
                state (om/get-state this)
                close-help #(om/update-state! this assoc :open-help? false)
                {:keys [drawer-open?]} state]
            (ui/mui-theme-provider
             {:mui-theme (ui/get-mui-theme)}
             (dom/div
              #js {:className "h-100"}
              (ui/app-bar
               {:title "Dashboard"
                :show-menu-icon-button true
                ;;:icon-element-left (ui/icon-button (ic/navigation-close))
                :icon-element-right  (ui/flat-button
                                      {:label     "Home"
                                       :href      "https://tipevi.de/home"
                                       :secondary true})
                :on-left-icon-button-touch-tap
                #(om/set-state! this {:drawer-open? true})})
              (ui/drawer
               {:docked            false
                :open              drawer-open?
                :on-request-change (fn [open] (change-drawer-state this open))}
               (ui/menu-item {:on-touch-tap #(do (change-drawer-state this false)
                                                 (site5.route/app-dispatch :index))} "Home")
               (ui/menu-item {:on-touch-tap #(do (change-drawer-state this false)
                                                 (site5.route/app-dispatch :notes))} "Notes"))

              (factory props)
              (ui/dialog
               {:title            "Help"
                :key              "dialog"
                :modal            false
                :open             (boolean (:open-help? state))
                :actions          [(ui/raised-button
                                    {:label        "Back"
                                     :key          "back"
                                     :on-touch-tap close-help})
                                   (ui/raised-button
                                    {:label        "Thanks"
                                     :key          "thanks"
                                     :on-touch-tap close-help})]
                :on-request-close close-help})
              )))))

(def app-wrapper (om/factory App))
