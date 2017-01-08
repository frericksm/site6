(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}

  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [adzerk/boot-cljs "1.7.170-3" :scope "test"]

                  ;; REPL
                  [adzerk/boot-cljs-repl   "0.3.3"] ;; latest release
                  [com.cemerick/piggieback "0.2.1"  :scope "test"]
                  [weasel                  "0.7.0"  :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]

                  [adzerk/boot-reload "0.4.13" :scope "test"]
                  [pandeiro/boot-http "0.7.6" :scope "test"]
                  [afrey/ring-html5-handler "1.1.0" :scope "test"]

                  [devcards "0.2.2" :exclusions [cljsjs/react] :scope "test"]

                  [org.clojure/clojurescript "1.9.293"]

                  ;; React
                  [cljsjs/react "15.3.1-1"] 
                  [cljsjs/react-dom "15.3.1-1"]
                  [sablono "0.7.6"]
                  [org.omcljs/om "1.0.0-alpha47"]

                  [adzerk/env "0.3.0"]
                  [com.domkm/silk "0.1.2"]

                  [com.cognitect/transit-cljs "0.8.239"]

                  ;; Assets
                  [org.clojars.nberger/boot-fingerprint "0.1.2-SNAPSHOT"]

                  ;; Styles
                  [org.webjars.bower/bootstrap "4.0.0-alpha.2"]
                  [cljs-react-material-ui "0.2.21"]

                  [deraen/boot-sass "0.3.0" :scope "test"]

                  ; System
                  [com.stuartsierra/component "0.3.1"]
                  [compassus "1.0.0-alpha2"]
                  [secretary "1.2.3"]

                  
                  ])

(require
  '[adzerk.boot-cljs            :refer [cljs]]
  '[adzerk.boot-cljs-repl       :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload          :refer [reload]]
  '[pandeiro.boot-http          :refer [serve]]
  '[deraen.boot-sass            :refer [sass]]
  '[pointslope.boot-fingerprint :refer [fingerprint]])

(deftask build []
  (comp
    ;;(speak)
    (cljs)
    (sass)
    (fingerprint)
    (target)))

(defn set-development-env! []
  (task-options!
    cljs {:ids ["main"]}
    fingerprint {:skip true}
    ;;reload {:on-jsload 'site5.core/reload!}
))

(deftask dev
  "Run application in development mode"
  []
  (set-development-env!)
  (comp
    (serve :handler 'afrey.ring-html5-handler/handler :reload true :port 3001)
    (watch)
    (cljs-repl)
    (reload)
    (build)))
