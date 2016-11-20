(ns site5.transit
  (:require [cognitect.transit :as transit]
            [om.transit :as om-transit]
            [manifold.deferred :as d])
  (:import [java.io ByteArrayOutputStream]))

(defn fetch-and-clear-byte-array [^ByteArrayOutputStream byte-array]
  (let [result (.toString byte-array)]
    (.reset byte-array)
    result))

(defn transit-encode [value]
  (let [transit-out (ByteArrayOutputStream. 4096)
        writer (om-transit/writer transit-out)]
    (do
      (transit/write writer value)
      (fetch-and-clear-byte-array transit-out))))

(defn request->content-type
  "Pulls content type from request headers"
  [req]
  (get-in req [:headers "content-type"]))

(defn wrap-transit
  [handler]
  (fn [request]
    ;; TODO - handle requests without bodies?
    (if (= (request->content-type request) "application/transit+json")
      (-> request
        (update :body #(transit/read (om-transit/reader %)))
        (handler)
        (d/chain #(update % :body transit-encode)))
      ;; Not Transit
      (handler request))))
