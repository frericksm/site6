(ns site6.reconciler
  (:require [om.next :as om]
            [site6.env :as env]
            [site6.state :as state]
            [site6.remote :as remote]))

(def remotes
  {:remote {:url env/BACKEND_URL}})

#_(defonce reconciler
  (om/reconciler
    {:state state/app-state
     :parser state/parser
     :id-key :db/id
     :normalize true
     :send #(remote/send-to-remotes! remotes %1 %2)}))
