(ns site6.handlers
  (:require [re-frame.core :refer [reg-event-fx reg-fx inject-cofx]]
            [re-posh.core :refer [connect! reg-query-sub reg-pull-sub reg-event-ds]]
            [site6.state]
            [datascript.core :as d]
            [site6.routes :as routes]))

(enable-console-print!)

(reg-event-ds
 :initialize-db
 (fn  [_ _]
   site6.state/datoms))

(reg-event-ds
 :route
 (fn [ds [_ {:keys [current-page route-params]}]] 
   (as-> [] x
     (if (not (nil? current-page) ) (cons [:db/add [:current/entity "current"] :current/page current-page] x) x)
     (if (not (nil? route-params) ) (cons [:db/add [:current/entity "current"] :current/route-params route-params] x) x)
     (vec x ))))

(reg-event-fx ;; register an event handler
 :navigate      ;; for events with this name
 [(inject-cofx :ds)] ;; inject coeffect
 (fn [cofx [_ new-page]] ;; get the co-effects and destructure the event
   {:navigate  new-page
    :dispatch [:drawer-change false]}))

(reg-event-fx ;; register an event handler
 :new-note    ;; for events with this name
 (fn [cofx [_]] ;; get the co-effects and destructure the event
   {:navigate  :note-editor
    :transact [{:db/id [:current/entity "current"]
                ;;:current/note-ref nil
                :current/note-title ""
                :current/note-body ""}]}))

(reg-event-fx ;; register an event handler
 :edit-note    ;; for events with this name
 (fn [cofx [_ dib title body author]] ;; get the co-effects and destructure the event
   {:navigate  :note-editor
    :transact [{:db/id [:current/entity "current"]
                :current/note-ref dib
                :current/note-title title
                :current/note-body body}]}))

(reg-fx
 :navigate 
 (fn [new-page] (routes/set-token! (routes/url-for new-page))))

(reg-event-ds
 :drawer-change         
 (fn [ds [_ new-value]] 
   [{:db/id [:current/entity "current"] 
     :current/drawer-open? new-value}]))


(reg-event-ds
 :edit-title         
 (fn [ds [_ new-value]] 
   [{:db/id [:current/entity "current"] 
     :current/note-title new-value}]))

(reg-event-ds
 :edit-body
 (fn [ds [_ new-value]] 
   [{:db/id [:current/entity "current"] 
     :current/note-body new-value}]))

(reg-event-ds
 :save-node
 (fn [ds [_]] 
   (let [e (d/pull ds '[*] [:current/entity "current"])
         n-ref (or (:db/id (:current/note-ref e)) -1)]
     [{:db/id [:current/entity "current"] 
       :current/note-ref n-ref}
      {:db/id n-ref
       :note/title (:current/note-title e)
       :note/body (:current/note-body e)
       ;;:note/author -5
       ;;:note/tags ["GTD" "Schule"]
       }])))
