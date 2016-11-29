(ns site5.components.handler
  (:require [com.stuartsierra.component :as component]))

(defn routing
  "Apply a list of routes to a Ring request map."
  [request & handlers]
  (some #(% request) handlers))

(defn routes
  "Create a Ring handler by combining several handlers into one."
  [& handlers]
  (fn
    ([request]
     (apply routing request handlers))
    ([request respond raise]
     (letfn [(f [handlers]
               (if (seq handlers)
                 (let [handler  (first handlers)
                       respond' #(if % (respond %) (f (rest handlers)))]
                   (handler request respond' raise))
                 (respond nil)))]
       (f handlers)))))

(defrecord Handler []
  component/Lifecycle
  (start [component]
    (let [rts (keep :routes (vals component))
          wrap-mw (get-in component [:middleware :wrap-wm] identity)
          handler (wrap-mw (apply routes rts))]
      (assoc component :handler handler)))
  (stop [component]
    (dissoc component :handler)))

(defn new-handler
  ([] (->Handler)))


