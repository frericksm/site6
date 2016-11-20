(ns site5.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [site5.reconciler :refer [reconciler]]
            [site5.components.base :as base]))

(defn top-level-node
  "Return document body. Needed for storing history"
  []
  (.-body js/document))

(defn init! []
  (enable-console-print!)
  (om/add-root! reconciler base/Base (gdom/getElement "app")))

(defn reload! []
  (.forceUpdate (om/class->any reconciler base/Base)))
