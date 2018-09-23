(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}

  :dependencies '[[adzerk/env "0.4.0"]
                  [adzerk/boot-cljs "2.1.4" :scope "test"]
                  [adzerk/boot-cljs-repl "0.3.3" :scope "test"] ;; latest release
                  [adzerk/boot-reload "0.6.0" :scope "test"]
                  [pandeiro/boot-http "0.8.3" :scope "test"]
                  [com.cemerick/piggieback "0.2.1"  :scope "test"]

                  [weasel                  "0.7.0"  :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]

                  [org.clojure/clojure "1.9.0"]
 [org.slf4j/slf4j-nop         "1.7.22"]

                  ;; REPL

                  [afrey/ring-html5-handler "1.1.1" :scope "test"]

                  [org.clojure/core.async "0.3.442"]
                  [org.clojure/clojurescript "1.9.946"]

                  [binaryage/devtools            "0.9.4"           :scope "test"]


                  [cljsjs/react "15.6.1-1"]
                  [cljsjs/react-dom "15.6.1-1"]

                  [re-frame "0.10.6" ]
                  [day8.re-frame/async-flow-fx "0.0.11"]
 

                  ;; Assets
                  [org.clojars.nberger/boot-fingerprint "0.1.2-SNAPSHOT"]

                  ;; Styles
                  [org.webjars.bower/bootstrap "4.0.0-alpha.4"]
                  [cljs-react-material-ui "0.2.48"]
                  [deraen/boot-sass "0.3.1" :scope "test"]

                  [bidi                          "2.1.3"]

                  [datascript "0.16.6"]
                  [re-posh "0.3.0" :exclusions [cljsjs/react cljsjs/react-dom]]
                  ])

(require
  '[adzerk.boot-cljs            :refer [cljs]]
  '[adzerk.boot-cljs-repl       :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload          :refer [reload]]
  '[pandeiro.boot-http          :refer [serve]]
  '[deraen.boot-sass            :refer [sass]]
  '[pointslope.boot-fingerprint :refer [fingerprint]])

(deftask build-prod []
  (comp
    ;;(speak)
    (watch)
    #_(cljs)
    (cljs :optimizations :advanced
          :source-map false
          :compiler-options {:pseudo-names true
                             ;; :optimizations :advanced
                             :output-wrapper :true
                             })
    #_(sift :include #{#"\.out"} :invert true)
    (sass)
    #_(fingerprint)
    (target :dir #{"/home/michael/projects/test-www/ic"})))

(deftask build []
  (comp
    (speak)
    (cljs)
    #_(cljs :optimizations :advanced
          :source-map true
          :compiler-options {:pseudo-names true
                             ;; :optimizations :advanced
                             :output-wrapper :true
                             })
    (sass)
    (fingerprint)
    (target)))

#_(defn set-development-env! []
  (task-options!
    cljs {:ids ["main"]}
    fingerprint {:skip true}

))

(deftask dev
  "Run application in development mode"
  []
  #_(set-development-env!)
  (comp
    (watch)
    (cljs-repl)
    (reload :on-jsload 'site6.core/mount-root)
    #_(speak)
    (serve :handler 'afrey.ring-html5-handler/handler :port 3001)
    (cljs :ids ["main"]
          ;;:compiler-options {:preloads '[devtools.preload]}
          )
    (sass)
    #_(fingerprint)
    (target)))
