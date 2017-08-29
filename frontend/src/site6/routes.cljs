(ns site6.routes
  (:require [bidi.bidi :as bidi]
            [site6.router :as r]
            [re-frame.core :as re-frame]))

; https://github.com/juxt/bidi#route-patterns
(def routes ["/ic" [["" :home]
                    ["/notes" :notes]
                    ["/note-editor" :note-editor]]])



(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [match]
  (let [page (:handler match)
        route-params (:route-params match)]
    (re-frame/dispatch [:route {:current-page page :route-params route-params}])))


(defonce router-atom (atom nil))

(defn start! []
  (reset! router-atom (r/start-router! routes
                                       {:on-navigate (fn [location]
                                                       (dispatch-route location))
                                        :default-location {:handler :home}})))

; Utilities
(defn set-token! [token]
  (r/set-location! @router-atom (bidi/match-route routes token)))

(def url-for (partial bidi/path-for routes))
