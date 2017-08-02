(ns site6.remote
  (:require [site6.env :as env]
            [site6.util :refer [mlog]]
            [cognitect.transit :as t]
            [goog.object :as obj]
            [om.transit :as om-transit])
  (:import [goog.net XhrIo]))

(defn ajax-opts []
  #js {"Content-Type" "application/transit+json"
       "Authorization" (obj/get js/localStorage "user-token")})

(defn transit-post
  ([edn callback] (transit-post edn callback env/BACKEND_URL))
  ([edn callback route]
   (.send XhrIo route
     (fn [e]
       (this-as this
         (callback (t/read (om-transit/reader) (.getResponseText this)))))
     "POST"
     (t/write (om-transit/writer) edn)
     (ajax-opts))))

(defn login-req [params callback]
  (transit-post params
    (fn [resp]
      (callback (:token resp)))
    (str env/BACKEND_URL "/login")))

(defn send-to-remotes! [remotes sends merge-fn]
  ;; TODO handle multiple backends correctly
  (doseq [[remote query] sends]
    (mlog ">> send" query)
    (transit-post query merge-fn)))
