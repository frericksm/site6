(ns site5.server
  (:require [ring.util.response :as response]
            ;; TODO maybe not?
            [system.repl :refer [system]]
            [site5.transit :refer [wrap-transit]]
            [site5.cors :refer [wrap-cors]]
            [site5.errors :as errors]
            [site5.parser :as parser]
            [site5.util :as util]))

(defn conn
  "Get Datomic connection from the system map"
  []
  (get-in system [:db :conn]))

(def pull-up-tempids
  "Move :tempids out of :result hash and up to root"
  (map
    (fn [[key value :as map-entry]]
      (if-not (symbol? key)
        map-entry

        (let [tempids (get-in value [:result :tempids] {})
              new-value (-> value
                          (assoc :tempids tempids)
                          (util/dissoc-in [:result :tempids]))]
          [key new-value])))))

(defn handle-query [req]
  (let [query (:body req)]
    (->> query
      ;; TODO identity
      (parser/parser {:conn (conn) :identity (:identity req)})
      (into {} pull-up-tempids)
      (response/response))))

(def app
  (-> handle-query
    (wrap-transit)
    #_(wrap-cors
      :access-control-allow-origin [#".*"]
      :access-control-allow-methods [:post])
    (errors/wrap-error-notifications)))
