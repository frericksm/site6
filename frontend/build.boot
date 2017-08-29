(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}

  :dependencies '[[adzerk/env "0.4.0"]
                  [adzerk/boot-cljs "2.0.0" :scope "test"]
                  [adzerk/boot-cljs-repl "0.3.3" :scope "test"] ;; latest release
                  [adzerk/boot-reload "0.5.1" :scope "test"]
                  [pandeiro/boot-http "0.7.6" :scope "test"]
                  [com.cemerick/piggieback "0.2.1"  :scope "test"]

                  [weasel                  "0.7.0"  :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]

                  [org.clojure/clojure "1.8.0"]

                  ;; REPL

                  [afrey/ring-html5-handler "1.1.0" :scope "test"]

                  [org.clojure/core.async "0.3.442"]
                  [org.clojure/clojurescript "1.9.521"]

                  [binaryage/devtools            "0.9.4"           :scope "test"]


                  #_[reagent  "0.6.2" :exclusions [cljsjs/react cljsjs/react-dom]]
                  [re-frame "0.8.0" :exclusions [cljsjs/react cljsjs/react-dom]]
                  [day8.re-frame/async-flow-fx "0.0.7"]
 

                  ;; Assets
                  [org.clojars.nberger/boot-fingerprint "0.1.2-SNAPSHOT"]

                  ;; Styles
                  [org.webjars.bower/bootstrap "4.0.0-alpha.4"]
                  [cljs-react-material-ui "0.2.45"]
                  [deraen/boot-sass "0.3.0" :scope "test"]

                  [bidi                          "2.0.8"]

                  [datascript "0.16.1"]
                  [re-posh "0.1.5" :exclusions [cljsjs/react cljsjs/react-dom]]
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
