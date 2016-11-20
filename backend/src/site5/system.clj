(ns site5.system
  (:import datomic.Util)
  (:require [site5.env :as env]
            [site5.server :refer [app]]
            [site5.db.util :as db-util]
            [com.stuartsierra.component :as component]
            [system.components
             [datomic :refer [new-datomic-db]]
             [aleph :refer [new-web-server]]]))

(defrecord DatomicDatabase [uri conn]
  component/Lifecycle
  (start [component]
    (assoc component :conn (db-util/initialize-db uri)))

  (stop [component]
    (assoc component :conn nil)))

(defn new-database [db-uri]
  (DatomicDatabase. db-uri nil))

(defn dev-system []
  (component/system-map
    :db (new-database env/DATOMIC_URI)
    :web (new-web-server (Integer. env/PORT) #'app)))

(defn prod-system []
  (component/system-map
    :db (new-database env/DATOMIC_URI)
    :web (new-web-server (Integer. env/PORT) app)))
