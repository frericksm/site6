(ns site6.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub-raw reg-event-fx]]
            [re-posh.core :refer [reg-query-sub]]
            [day8.re-frame.async-flow-fx]))


(reg-query-sub
  :note-list
  '[ :find  ?title ?body ?author
     :where [?e :note/title ?title]
            [?e :note/body  ?body]
            [?e :note/author ?author]])

(reg-query-sub
  :current-page
  '[ :find  [?v]
     :where [?e :current/page ?v]])

#_(reg-sub-raw
  :current-page
  (fn [db] (reaction (:current-page @db))))

(reg-query-sub
  :route-params
  '[ :find  [?v]
     :where [?e :current/route-params ?v]])

#_(reg-sub-raw
  :route-params
  (fn [db] (reaction (:route-params @db))))

(reg-query-sub
  :drawer-open?
  '[ :find  [?v]
     :where [?e :current/drawer-open? ?v]])

#_(reg-sub-raw
  :drawer-open?
  (fn [db] (reaction (:drawer-open? @db))))


(defn boot-flow
  []
  {:first-dispatch [:initialize-db]
   :rules []}
  #_{:first-dispatch [:do-X]              ;; what event kicks things off ?
   :rules [                             ;; a set of rules describing the required flow
     {:when :seen? :events :success-X  :dispatch [:do-Y]}
     {:when :seen? :events :success-Y  :dispatch [:do-Z]}
     {:when :seen? :events :success-Z  :halt? true}
     {:when :seen-any-of? :events [:fail-X :fail-Y :fail-Z] :dispatch  [:app-failed-state] :halt? true}]})


#_(reg-event-fx                    ;; note the -fx
  :boot                          ;; usage:  (dispatch [:boot])  See step 3
  (fn [_ _]
    {:db (-> {}                  ;;  do whatever synchronous work needs to be done
            #_task1-fn             ;; ?? set state to show "loading" twirly for user??
            #_task2-fn)            ;; ?? do some other simple initialising of state
     :async-flow  (boot-flow)})) ;; kick off the async process
