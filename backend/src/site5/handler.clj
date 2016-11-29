(ns site5.handler
  (:require [ring.util.response :refer [response content-type charset]]
            [bidi.ring :refer (make-handler)]))

(defn index-handler [db]
  (fn [request]
    (response "Homepage")))

(defn article-handler [db]
  (fn 
    [{:keys [route-params]}]
    (response (str "You are viewing article: " (:id route-params)))))

(defn handler [{db :db}]
  (make-handler ["/" {"index.html" (index-handler db)
                      ["articles/" :id "/article.html"] (article-handler db)}]))


