(ns site5.env
  (:require-macros [adzerk.env :as env]))

(env/def
  BACKEND_URL "http://localhost:3000/db")
