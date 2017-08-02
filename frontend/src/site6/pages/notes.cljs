(ns site6.pages.notes
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require  [cljsjs.material-ui]
             [cljs-react-material-ui.core :refer [divider get-mui-theme color]]
             [cljs-react-material-ui.reagent :as ui]
             [cljs-react-material-ui.icons :as ic]
             [reagent.core :as reagent]
             [re-frame.core :refer [reg-sub-raw reg-event-fx] :as rf]
            ))

(defn note-list-entry [note]
  (let [title (nth note 0)
        b (nth note 1)
        a (nth note 2)
        ]
    [ui/list-item
     {:primary-text title 
      :secondary-text b 
      :on-touch-tap (fn [e] nil)}]))

(defn notes-list [notes]
  "notes"
  [ui/list (as-> notes x
             (map note-list-entry x) 
             (interpose [divider] x))])

(defn main []
  (let [note-list (rf/subscribe [:note-list])]
    (fn []
      (let [nl @note-list]
        [:div
         [:div {:className "row around-xs mar-top-20"}
          (reagent/as-element [ui/paper
                               {:class-name "col-xs-11 col-md-11 col-lg-7"}
                               [ui/mui-theme-provider
                                {:mui-theme (get-mui-theme
                                             {:table-header-column
                                              {:text-color (color :deep-orange500)}})}
                                (notes-list nl)]
                               ])]
         [:div
          {:className "row around-xs mar-top-20"}
          (reagent/as-element [ui/floating-action-button 
                               {:on-touch-tap #()} 
                               (ic/content-add)])]])
      )))

#_(defn main []
  (fn []
    [:div "Notes"]
    ))
