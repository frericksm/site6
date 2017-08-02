(ns site6.components.note-editor
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljsjs.material-ui]

            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]

            [cljs-react-material-ui.icons :as ic]
            [goog.dom :as gdom]

            [reagent.core :as reagent]
            [re-frame.core :as rf]

            [site6.route]
            
            [secretary.core :as secretary :refer-macros [defroute]]

            [com.stuartsierra.component :as component]
            [cljs.core.async :as async :refer [>! <! put! chan alts! close!]]))


(def path "/note-editor")
(def route :note-editor)

(defn target-val [e]
  (.. e -target -value))

#_(defui NoteEditor
;;  static om/Ident
  #_(ident [this {:keys [db/id]}]
    [:note/by-id id])

  static om/IQuery
  (query [this]
         [{:note/current [:db/id :note/title :note/body]}])

  Object
  (render [this]
    (let [note-current (:note/current (om/props this))
          {:keys [note/title note/body note/author]} note-current]

      (dom/div nil
               (dom/div
                #js {:className "row around-xs mar-top-20"}
                (ui/paper
                 {:class-name "col-xs-11 col-md-6 col-lg-4"}
                 (ui/text-field
                  {:floating-label-text "Titel"
                   ;;:floating-label-fixed  true
                   ;;_:hint-text           "Ein Titel"
                   :class-name          "w-100"
                   :value               title 
                   :on-change
                   
                   #(om/transact! this `[(note-current/change {:value ~(target-val %)
                                                               :path  [:note/title]})
                                         #_:note/current])})
                 (ui/text-field
                  {:floating-label-text "Text"
                   :multi-line true
                   :rows 10
                   :rows-max 10
                   :full-width true
                   ;;:hint-text  "Dein Text ..."
                   :class-name "w-100"
                   :value      body 
                   :on-change
                   #(om/transact! this `[(note-current/change {:value ~(target-val %)
                                                               :path  [:note/body]})
                                         #_:note/current])})
                 )
                )
               (dom/div
                #js {:className "row around-xs mar-top-20"}
                (ui/floating-action-button 
                 {:on-touch-tap #(do #_(om/update-state! 
                                      this 
                                      assoc :open-help? true)
                                     (site6.route/app-dispatch :notes))} 
                 (ic/action-done)))))))

(defn note-editor [title body] 
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
   [:div  {:className "row around-xs mar-top-20"}
    (ui/floating-action-button 
     {:on-touch-tap #(do #_(om/update-state! 
                            this 
                            assoc :open-help? true)
                         (site6.route/app-dispatch :notes))} 
     (ic/action-done))]]
)

(defn add-route [route-manager]
  (rf/dispatch [:route-change route]))

(defrecord NoteEditorComp [eventbus route-manager]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting NoteEditorComp") 
    (let [ebch (:channel eventbus)] 
      (secretary/add-route! path add-route)
      (site6.route/add-route route-manager route note-editor)
      this))

  (stop [this]
    (println "Stopping NoteEditorComp")
    this))

(defn new-note-editor []
  (map->NoteEditorComp {}))
