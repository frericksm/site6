(ns site6.pages.note-editor
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]
            [reagent.core :as reagent]
            [re-frame.core :as rf]))


(defn main [] 
  (let [current-note (rf/subscribe [:current-note [:current/entity "current"]])]
    (fn []
      (let [cn @current-note
            title (:current/note-title cn)
            body (:current/note-body cn)]
        [:div 
         [:div {:className "row around-xs mar-top-20"}
          [ui/paper {:class-name "col-xs-11 col-md-6 col-lg-4"}
           [ui/text-field
            {:floating-label-text "Titel"
             ;;:floating-label-fixed  true
             ;;_:hint-text           "Ein Titel"
             :class-name          "w-100"
             :value               title 
             :on-change
             
             (fn [e] #_(om/transact! this `[(note-current/change {:value ~(target-val %)
                                                                  :path  [:note/title]})
                                            #_:note/current]))}]
           [ui/text-field
            {:floating-label-text "Text"
             :multi-line true
             :rows 10
             :rows-max 10
             :full-width true
             ;;:hint-text  "Dein Text ..."
             :class-name "w-100"
             :value      body 
             :on-change
             (fn [e] #_(om/transact! this `[(note-current/change {:value ~(target-val %)
                                                                  :path  [:note/body]})
                                            #_:note/current]))}]
           ]
          ]
         #_[:div  {:className "row around-xs mar-top-20"}
          (ui/floating-action-button 
           {:on-touch-tap #(do #_(om/update-state! 
                                  this 
                                  assoc :open-help? true)
                               #_(site6.route/app-dispatch :notes))} 
           (ic/action-done))]])))
)

