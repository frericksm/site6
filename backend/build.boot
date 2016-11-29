(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}

  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [adzerk/env "0.3.0"]

                  [org.danielsz/system "0.3.1"]
                  [aleph "0.4.1"]

                  ;; DB
                  [com.datomic/datomic-free "0.9.5407"]
                  [com.flyingmachine/datomic-booties "0.1.2"]
                  [io.rkn/conformity "0.4.0"]


                  [buddy "1.2.0"]

                  ;; middleware
                  [ring-cors "0.1.8"]

                  [org.omcljs/om "1.0.0-alpha47"]])

(boot.core/load-data-readers!)

(require
  '[system.boot :refer [system run]]
  '[site5.system :refer [dev-system]])

(deftask dev
  "Run a restartable system in the repl"
  []
  (comp
    (watch :verbose true)
    (system :sys #'dev-system :auto true)
    ;;(speak)
    (repl :server true)))
