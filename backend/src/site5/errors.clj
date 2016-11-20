(ns site5.errors
  (:require [manifold.deferred :as d]))

(defn handle-ex [ex]
  (.printStackTrace ex)
  (throw ex))

(defn wrap-error-notifications
  "Prints stacktraces"
  [handler]
  (fn [req]
    (try
      (d/catch (handler req) Exception handle-ex)
      (catch Exception ex
        (handle-ex ex)))))
