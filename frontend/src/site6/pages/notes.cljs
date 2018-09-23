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
  (let [dib (nth note 0)
        title (nth note 1)
        body (nth note 2)
        author (nth note 3)
        ]
    [ui/list-item
     
     {:key dib
      :primary-text title 
      :secondary-text body 
      :on-click (fn [e] 
                  (.preventDefault e)
                  (rf/dispatch [:edit-note dib title body author]))}]))

(defn notes-list [notes]
  "notes"
  [ui/list (as-> notes x
             (map note-list-entry x) 
             (interpose "divider" x) ;; separator  "divider einfügen 
             (map-indexed (fn [i n]
                            (if (= n  "divider" );; und durch eine divider component
                              (divider {:key i}) ;; ersetzen 
                              n)) x))])

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
         ^{:key "2"}   [:div
                        {:className "row around-xs mar-top-20"}
                        (reagent/as-element [ui/floating-action-button 
                                             {:on-click (fn [e] 
                                                          (.preventDefault e)
                                                          (rf/dispatch [:new-note]))} 
                                             (ic/content-add)])]])
      )))

#_(defn main []
    (fn []
      [:div "Notes"]
      ))
