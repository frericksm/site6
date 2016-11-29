(ns site5.handler2
  (:require [ring.util.response :refer [response]]
            ;; TODO maybe not?
            [site5.transit :refer [wrap-transit]]
            [site5.cors :refer [wrap-cors]]
            [site5.errors :as errors]
            [site5.parser :as parser]
            [site5.util :as util]
            
            [bidi.ring :refer (make-handler)]))


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

(defn handle-query [db]
  (fn  [req]
    (let [query (:body req)]
      (as-> query x
           ;; TODO identity
           (parser/parser {:conn (:conn db) :identity (:identity req)} x)
           (do (println x) x)
           (into {} pull-up-tempids x)
           (response x)))))


(defn handler [{db :db}]
  (make-handler ["/" {"db" (-> (handle-query db)
                               (wrap-transit)
                               (wrap-cors
                                :access-control-allow-origin [#".*"]
                                :access-control-allow-methods [:post])
                               (errors/wrap-error-notifications))}]))
