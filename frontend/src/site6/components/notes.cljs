(ns site6.components.notes
  (:require  [cljsjs.material-ui]
             [cljs-react-material-ui.core :refer [divider get-mui-theme color]]
             [cljs-react-material-ui.reagent :as ui]
             [cljs-react-material-ui.icons :as ic]
             [reagent.core :as reagent]
             [re-frame.core :as rf]
             
             [site6.route]

             [goog.dom :as gdom]
             
             [secretary.core :as secretary :refer-macros [defroute]]
             
             [com.stuartsierra.component :as component]
             [cljs.core.async :as async :refer [>! <! put! chan alts! close!]]))


(def path "/notes")
(def route :notes)


(defn note
  [title body]
  [ui/table-row
   [ui/table-row-column title]
   [ui/table-row-column body]]
  #_[:div.example-clock
   {:style {:color @(rf/subscribe [:time-color])}}
   (-> @(rf/subscribe [:time])
       .toTimeString
       (clojure.string/split " ")
       first)])


#_(defui Note
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

#_(def note (om/factory Note {}))


#_(defn edit-node [c id]
  (println "start edit-node: " c id)
  (om/transact! c `[(note/current {:db/id ~id})])
  (println "end edit-node "))

#_(defui NoteListEntry
  static om/Ident
  (ident [this {:keys [db/id]}]
    [:note/by-id id])

  static om/IQuery
  (query [this]
         [:db/id :note/title :note/body {:note/author [:person/name]}])

  Object
  (render [c]
    (let [{:keys [note/title note/body note/author db/id]} (om/props c)]
      (ui/list-item
       {:primary-text title 
        :secondary-text (str (:person/name author) " -- " body)
        :on-touch-tap (fn [e] (do (println id)
                              (edit-node (om/get-reconciler c) id)
                              (site6.route/app-dispatch :note-editor)
                              ))
        #_(dom/div #js{} (dom/span {:color "dark"} 
                                   (:person/name author)  ) "--" body)}))))


#_(def note-list-entry (om/factory NoteListEntry {:keyfn :note/title}))

(defn note-list-entry [{:keys [title body]}]
  [ui/list-item
   {:primary-text title 
    :secondary-text (str "(:person/name author)" " -- " body)
    :on-touch-tap (fn [e] #_(do (println id)
                              (edit-node (om/get-reconciler c) id)
                              (site6.route/app-dispatch :note-editor)
                              ))}])
(defn notes-list [notes]
  "notes"
  [ui/list
   (as-> notes x
     (map note-list-entry x) 
     (interpose (divider) x))])

#_(defui NoteList
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
                 {:on-touch-tap #(do #_(om/update-state! 
                                      this 
                                      assoc :open-help? true)
                                     (site6.route/app-dispatch :note-editor))} 
                 (ic/content-add)))))))

#_(def note-lister (om/factory NoteList))

(defn note-lister []
  (let [note-list  @(rf/subscribe [:note/list])]
    [:div  
     [:div {:className "row around-xs mar-top-20"}
      (reagent/as-element [ui/paper
                           {:class-name "col-xs-11 col-md-11 col-lg-7"}
                           [ui/mui-theme-provider
                            {:mui-theme (get-mui-theme
                                         {:table-header-column
                                          {:text-color (color :deep-orange500)}})}
                            (notes-list note-list)]
                           ])]
     [:div
      {:className "row around-xs mar-top-20"}
      (reagent/as-element [ui/floating-action-button 
                           {:on-touch-tap #(do #_(om/update-state! 
                                                  this 
                                                  assoc :open-help? true)
                                               (site6.route/app-dispatch :note-editor))} 
                           (ic/content-add)])]]
    "Notes"))

(defn add-route [] (rf/dispatch [:route-change route]))

(defn init-queries []
  (rf/reg-sub
   :note/list
   (fn [db _] 
     (-> db
         :note/list)))
)

(defrecord NoteListComp [eventbus route-manager]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting NotesComp") 
    (let [ebch (:channel eventbus)
          r route] 
      (init-queries)
      (secretary/add-route! path add-route)
      (site6.route/add-route route-manager r note-lister)
      this))

  (stop [this]
    (println "Stopping NotesComp")
    this))

(defn new-notes []
  (map->NoteListComp {}))
