(ns site6.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]))

; https://github.com/juxt/bidi#route-patterns
(def routes ["/" {""      :home
                  "notes" :notes
                  "note-editor" :note-editor}])



(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [match]
  (let [page (:handler match)
        route-params (:route-params match)]
    (re-frame/dispatch [:route {:current-page page :route-params route-params}])))


(def history (pushy/pushy dispatch-route (partial bidi/match-route routes)))

(defn start! []
  (pushy/start! history))

; Utilities
(defn set-token! [token]
  (pushy/set-token! history token))

(defn get-token [] (pushy/get-token history))

(def url-for (partial bidi/path-for routes))
