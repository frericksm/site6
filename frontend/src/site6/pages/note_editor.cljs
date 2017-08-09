(ns site6.pages.note-editor
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]
            [reagent.core :as reagent]
            [re-frame.core :as rf]))

(defn target-val [e]
  (.. e -target -value))

(defn main [] 
  (let [current-note (rf/subscribe [:current-note [:current/entity "current"]])]
    (fn []
      (let [cn @current-note
            title (get cn :current/note-title "")
            body (get cn :current/note-body "")]
        [:div 
         [:div {:className "row around-xs mar-top-20"}
          [ui/paper {:class-name "col-xs-11 col-md-6 col-lg-4"}
           [ui/text-field
            {:floating-label-text "Titel"
             ;;:floating-label-fixed  true
             ;;_:hint-text           "Ein Titel"
             :class-name          "w-100"
             :value               title 
             :on-change           (fn [e] (.preventDefault e)
                                    (rf/dispatch [:edit-title (target-val e)]))}]
           [ui/text-field
            {:floating-label-text "Text"
             :multi-line true
             :rows 10
             :rows-max 10
             :full-width true
             ;;:hint-text  "Dein Text ..."
             :class-name "w-100"
             :value      body 
             :on-change  (fn [e] (.preventDefault e)
                           (rf/dispatch [:edit-body (target-val e)]))}]
           ]
          ]
         [:div  {:className "row around-xs mar-top-20"}
          (reagent/as-element [ui/floating-action-button 
                               {:on-touch-tap (fn [e] (.preventDefault e)
                                                (rf/dispatch [:save-node]))} 
                               (ic/action-done)])]])))
)

