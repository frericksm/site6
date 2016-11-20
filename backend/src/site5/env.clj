(ns site5.env
  (:require [adzerk.env :as env]))

(env/def
  PORT "3000"
  DATOMIC_URI "datomic:mem://localhost:4334/site5")
