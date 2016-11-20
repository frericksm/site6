(ns site5.db.util
  (:require [datomic.api :as d]
            [io.rkn.conformity :as c]
            [growmonster.core :as g]))

(defn add-schema [conn]
  (let [norms (c/read-resource "db/schema.edn")]
    (c/ensure-conforms conn norms [:site5/initial-schema])))

(defn add-seeds
  "Add initial data"
  [conn]
  (let [seeds (c/read-resource "db/seed.edn")]
    (c/ensure-conforms conn {:stack/seed {:txes [(g/inflatev seeds)]}})))

(defn initialize-db
  "Initialize a Datomic db and return the db connection"
  [uri]
  (d/create-database uri)
  (doto (d/connect uri)
    (add-schema)
    (add-seeds)))

(defn resolve-tempid [tx-result temp-id]
  (d/resolve-tempid (:db-after tx-result) (:tempids tx-result) temp-id))
