(ns site5.system
  (:import datomic.Util)
  (:require [site5.env :as env]
            [site5.server :refer [app]]
            [site5.db :refer [new-database]]
            [site5.db.util :as db-util]
            [site5.handler]
            [site5.handler2]
            [site5.components.handler :refer [new-handler]]
            [com.stuartsierra.component :as component]
            (system.components 
             [endpoint :refer [new-endpoint]]
             [middleware :refer [new-middleware]]
             [aleph :refer [new-web-server]])))

(defn dev-system []
  (component/system-map
   :db (new-database env/DATOMIC_URI)
   
   :routes (component/using
              (new-endpoint site5.handler/handler)
              [:db])
   :routes2 (component/using
             (new-endpoint site5.handler2/handler)
             [:db])
   :middleware (new-middleware {} #_{:middleware [[wrap-restful-format]
                                                  [wrap-defaults :defaults]]
                                     :defaults api-defaults})
   :handler (component/using
             (new-handler)
             [:routes :routes2 :middleware])
   :http (component/using
          (new-web-server (Integer. env/PORT))
          [:handler])))

(defn prod-system [] 
  nil)



