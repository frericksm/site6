(ns site6.pages.notes
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require  [cljsjs.material-ui]
             [cljs-react-material-ui.core :refer [divider get-mui-theme color]]
             [cljs-react-material-ui.reagent :as ui]
             [cljs-react-material-ui.icons :as ic]
             [reagent.core :as reagent]
             [re-frame.core :refer [reg-sub-raw reg-event-fx] :as rf]
            ))

(defn edit-note [dib title body author]
  (rf/dispatch [:edit-note dib title body author])
  (rf/dispatch [:navigate :note-editor]))

(defn note-list-entry [note]
  (let [dib (nth note 0)
        title (nth note 1)
        body (nth note 2)
        author (nth note 3)
        ]
    ^{:key dib}
    [ui/list-item
     {:primary-text title 
      :secondary-text body 
      :on-touch-tap (fn [e] 
                  (.preventDefault e)
                  (edit-note dib title body author))}]))

(defn notes-list [notes]
  "notes"
  [ui/list (as-> notes x
             (map note-list-entry x) 
             #_(interpose [divider] x)
             (map-indexed (fn [i p] ^{:key i} p) x))])

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
