(ns site5.db
  (:import datomic.Util)
  (:require [site5.db.util :as db-util]
            [com.stuartsierra.component :as component]))

(defrecord DatomicDatabase [uri conn]
  component/Lifecycle
  (start [component]
    (assoc component :conn (db-util/initialize-db uri)))

  (stop [component]
    (assoc component :conn nil)))

(defn new-database [db-uri]
  (DatomicDatabase. db-uri nil))

