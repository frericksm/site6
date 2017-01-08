(ns site5.components.notes
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :as ui]
            [cljs-react-material-ui.icons :as ic]
            [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]

            [compassus.core :as compassus]
            [secretary.core :as secretary :refer-macros [defroute]]

            [com.stuartsierra.component :as component]
            [cljs.core.async :as async :refer [>! <! put! chan alts! close!]]))


(def path "/notes")
(def route :notes)

(defui Note
  static om/Ident
  (ident [this {:keys [db/id]}]
    [:note/by-id id])

  static om/IQuery
  (query [this]
    [:db/id :note/title :note/body])

  Object
  (render [this]
    (let [{:keys [note/title note/body]} (om/props this)]
      (ui/table-row
        (ui/table-row-column title)
        (ui/table-row-column body)))))

(def note (om/factory Note {}))

(defui NoteListEntry
  static om/Ident
  (ident [this {:keys [db/id]}]
    [:note/by-id id])

  static om/IQuery
  (query [this]
         [:db/id :note/title :note/body {:note/author [:person/name]}])

  Object
  (render [this]
    (let [{:keys [note/title note/body note/author]} (om/props this)]
      (ui/list-item
       {:primary-text title 
        :secondary-text (str (:person/name author) " -- " body)
        #_(dom/div #js{} (dom/span {:color "darkBlack"} 
                                   (:person/name author)  ) "--" body)}))))


(def note-list-entry (om/factory NoteListEntry {}))

(defn notes-table [notes]
  (ui/table
    {:height "250px"}
    (ui/table-header
      {:display-select-all  false
       :adjust-for-checkbox false}
      (ui/table-row
        (ui/table-header-column "Title")
        (ui/table-header-column "Body")))
    (ui/table-body
      (map note notes))))

(defn notes-list [notes]
  (ui/list
   (interpose (ui/divider) (map note-list-entry notes))))


(defui NoteList
  static om/IQuery
  (query [this]
    [{:note/list (om/get-query NoteListEntry)}])
  Object
  (render [this]
    (let [props (om/props this)
          state (om/get-state this)
          note-list (:note/list props)]
      (dom/div nil 
               (dom/div
                #js {:className "row around-xs mar-top-20"}
                (ui/paper
                 {:class-name "col-xs-11 col-md-11 col-lg-7"}
                 (ui/mui-theme-provider
                  {:mui-theme (ui/get-mui-theme
                               {:table-header-column
                                {:text-color (ui/color :deep-orange500)}})}
                  (notes-list note-list))
                 ))
               (dom/div
                #js {:className "row around-xs mar-top-20"}
                (ui/floating-action-button 
                 {:on-touch-tap #(om/update-state! 
                                  this 
                                  assoc :open-help? true)} 
                 (ic/content-add)))))))


(defrecord NoteListComp [eventbus]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting NotesComp") 
    (let [ebch (:channel eventbus)] 
      (secretary/add-route! path (fn [] (put! ebch ['set-route! route])))
      (assoc-in this [:routes route] NoteList)))

  (stop [this]
    (println "Stopping NotesComp")
    (assoc-in this [:routes route] nil)))

(defn new-notes []
  (map->NoteListComp {}))
