(ns site5.cors
  (:require [ring.middleware.cors :refer :all :exclude [wrap-cors]]
            [manifold.deferred :as d]))

(defn wrap-cors
  "Middleware that adds Cross-Origin Resource Sharing headers.
  (def handler
    (-> routes
        (wrap-cors
         :access-control-allow-origin #\"http://site5.com\"
         :access-control-allow-methods [:get :put :post :delete])))

  Modified to handle Manifold deferred responses"
  [handler & access-control]
  (let [access-control (normalize-config access-control)]
    (fn [request]
      (if (and (preflight? request) (allow-request? request access-control))
        (let [blank-response {:status 200
                              :headers {}
                              :body "preflight complete"}]
          (add-access-control request access-control blank-response))
        (if (origin request)
          (if (allow-request? request access-control)
            (d/let-flow [response (handler request)]
              (when response
                (add-access-control request access-control response))))
          (handler request))))))
