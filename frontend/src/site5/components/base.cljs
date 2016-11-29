(ns site5.components.base
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :refer-macros [html]]
            [cljs.pprint :as pprint]
            [site5.util :as util]
            [site5.state :as state]))

(defui ^:once Todo
  static om/IQuery
  (query [this]
    [:db/id :todo/text])

  static om/Ident
  (ident [this props]
    [:todo/by-id (:db/id props)])

  Object
  (componentWillMount [this]
    (om/update-state! this assoc :todo-input (-> this om/props :todo/text)))

  (componentWillUpdate [this next-props next-state]
    (let [input-changed? (:input-changed? next-state)]
      (when (and
              (not input-changed?)
              (not= (:todo/text next-props) (:todo-input next-state)))
        (om/update-state! this assoc :input-changed? true))

      (when (and
              input-changed?
              ;; Does string comparision on every keystroke
              (= (:todo/text next-props) (:todo-input next-state)))
        (om/update-state! this assoc :input-changed? false))))


  (render [this]
          (let [props (om/props this)
          local-state (om/get-state this)
          delete (-> this om/get-computed :todo/delete-fn)]
      (html
        [:li
         [:form
          {:on-submit
           (fn [event]
             (.preventDefault event)
             (om/transact! this
               `[(todo/update {:db/id ~(:db/id props)
                               :todo/text ~(:todo-input local-state)})]))}

          [:input {:value (:todo-input local-state)
                   :on-change #(util/update-input! % this :todo-input)}]

          (when (:input-changed? local-state)
            [:button.btn
             "Save"])]

         [:button.btn.btn-sm.btn-danger
          {:on-click #(delete (:db/id props))}
          "x"]]))))

(def todo (om/factory Todo))

(defn delete-todo [c id]
  (om/transact! c `[(todo/delete {:db/id ~id})]))

(defui ^:once Base
  static om/IQuery
  (query [this]
    [{:todo/list (om/get-query Todo)}])

  Object
  (render [this]
    (let [props (om/props this)]
      (html
        [:div.container
         [:button.btn {:on-click #(om/transact! this `[(todo/create {:db/id ~(om/tempid)})])}
          "New Todo"]

         (let [delete-fn (partial delete-todo this)]
           (for [t (:todo/list props)]
             (todo (-> t
                     (om/computed {:todo/delete-fn delete-fn})))))

         #_[:div
            (pr-str @state/app-state)]]))))
