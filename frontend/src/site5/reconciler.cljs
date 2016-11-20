(ns site5.reconciler
  (:require [om.next :as om]
            [site5.env :as env]
            [site5.state :as state]
            [site5.remote :as remote]))

(def remotes
  {:remote {:url env/BACKEND_URL}})

(defonce reconciler
  (om/reconciler
    {:state state/app-state
     :parser state/parser
     :id-key :db/id
     :normalize true
     :send #(remote/send-to-remotes! remotes %1 %2)}))
