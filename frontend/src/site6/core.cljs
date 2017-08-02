(ns site6.core
  (:require [reagent.dom :refer [render]]
            [re-frame.core :refer [dispatch-sync]]
            [site6.views]
            [site6.handlers]
            [site6.routes :as routes]
            [site6.subs]))

(enable-console-print!)


(defn mount-root []
  (render [site6.views/main] (.getElementById js/document "app")))

(defn ^:export init
  []
  (routes/start!)
  (dispatch-sync [:initialize-db])            ;; <--- boot process is started
  (mount-root)) 
