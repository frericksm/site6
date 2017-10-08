(ns site6.views
    (:require [re-frame.core :as rf]
              [reagent.core :as reagent]
            
              [cljsjs.material-ui]
              [cljs-react-material-ui.core :refer [get-mui-theme color]]
              [cljs-react-material-ui.reagent :as ui]
              [cljs-react-material-ui.icons :as ic]

              [site6.pages.home :as home]
              [site6.pages.notes :as notes]
              [site6.pages.note-editor :as note-editor]))

(defmulti pages identity)
(defmethod pages :home [] [home/main])
(defmethod pages :notes [] [notes/main])
(defmethod pages :note-editor [] [note-editor/main])
(defmethod pages :default [] [:div.tc "Nothing here, chap."])

(defn change-drawer-state [new-value]
  (rf/dispatch [:drawer-change new-value]))

(defn main []
  (let [current-page-subs (rf/subscribe [:current-page])
        drawer-open-subs (rf/subscribe [:drawer-open?]) 
        close-help (fn [e] #_(om/update-state! this assoc :open-help? false))]
    (fn []
      (let [open (first @drawer-open-subs)
            cp (first @current-page-subs)]
        [ui/mui-theme-provider {:mui-theme (get-mui-theme)}
         [:div {:className "h-100"}
          [ui/app-bar
           {:title "Dashboard"
            :show-menu-icon-button true
            ;;:icon-element-left (ui/icon-button (ic/navigation-close))
            :icon-element-right  (reagent/as-element [ui/flat-button
                                                      {:label     "Home"
                                                       :href      "ic"
                                                       :secondary true}])
            :on-left-icon-button-touch-tap
            (fn [e] (change-drawer-state true))}]
          [ui/drawer
           {:docked            false
            :open              open
            :on-request-change (fn [] (change-drawer-state (not open)))}
           [ui/menu-item {:on-touch-tap (fn [e] 
                                          (.preventDefault e)
                                          (rf/dispatch [:navigate :home]))} "Home"]
           [ui/menu-item {:on-touch-tap (fn [e] 
                                          (.preventDefault e)
                                          (rf/dispatch [:navigate :notes]))} "Notes"]]
          (pages cp)
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
          ]]))))
