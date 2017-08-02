(ns site6.handlers
  (:require #_[re-frame.core :refer [reg-event-db]]
            [re-posh.core :refer [connect! reg-query-sub reg-pull-sub reg-event-ds]]
            [site6.state]))


(reg-event-ds
  :initialize-db
  (fn  [_ _]
    site6.state/datoms))

#_(reg-event-db
  :initialize-db
  (fn  [_ _]
    site6.state/init-state))

(reg-event-ds
 :route
 (fn [ds [_ {:keys [current-page route-params]}]] 
   (as-> [] x
     (if (not (nil? current-page) ) (cons [:db/add [:current/entity "current"] :current/page current-page] x) x)
     (if (not (nil? route-params) ) (cons [:db/add [:current/entity "current"] :current/route-params route-params] x) x)
     (vec x ))))


#_(reg-event-db
  :route
  (fn [db [_ {:keys [current-page route-params]}]]
    (merge db {:current-page current-page :route-params route-params})))

(reg-event-ds
   :drawer-change         
   (fn [ds [_ new-value]] 
     [[:db/add [:current/entity "current"] :current/drawer-open? new-value]]))

#_(reg-event-db        
   :drawer-change         
   (fn [db [_ new-value]] (assoc db :drawer-open? new-value)))
