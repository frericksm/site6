(ns site5.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [site5.reconciler :refer [reconciler]]
            [site5.components.base :as base]
            [com.stuartsierra.component :as component]
            [site5.system]))

(enable-console-print!)

#_(defn top-level-node
  "Return document body. Needed for storing history"
  []
  (.-body js/document))

#_(defn init! []
  (enable-console-print!)
  (om/add-root! reconciler base/Base (gdom/getElement "app")))

#_(defn reload! []
  (.forceUpdate (om/class->any reconciler base/Base)))

(defn start []
  (component/start  (site5.system/system {})))

#_(start)
