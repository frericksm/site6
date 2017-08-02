(ns site6.state
  (:require [datascript.core :as d]
            [re-posh.core :refer [connect! reg-query-sub reg-pull-sub reg-event-ds]]))

(def schema 
  {:person/name {;;:db/valueType :db.type/string
                  :db/cardinality :db.cardinality/one
                  :db/doc "A person's name"
                  }
   
   :person/login {;;:db/valueType :db.type/string
                  :db/cardinality :db.cardinality/one
                  :db/doc "A person's login name"
                  }
   :person/date {;;:db/valueType :db.type/instant
                 :db/cardinality :db.cardinality/one
                 :db/doc "A person's last change date"
                 }
   
   :note/title {;;:db/valueType :db.type/string
                :db/cardinality :db.cardinality/one
                :db/doc "A note's title"
                } 
   :note/body {;;:db/valueType :db.type/string
               :db/cardinality :db.cardinality/one
               :db/doc "A note's body"
               } 
   :note/author {:db/valueType :db.type/ref
                 :db/cardinality :db.cardinality/one
                 :db/doc "A note's author"
                 }
   :note/tags {;;:db/valueType :db.type/string
               :db/cardinality :db.cardinality/many
               :db/doc "A note's tags"
               }

   :current/entity {:db/unique :db.unique/identity
                    ;;:db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "An marker for the entity"
                    }
   :current/page {;;:db/valueType :db.type/string
                  :db/cardinality :db.cardinality/one
                  :db/doc "The current page"
                  }  
   :current/route-params {;;:db/valueType :db.type/string
                          :db/cardinality :db.cardinality/one
                          :db/doc "The current route-params"
                          }
   
   :current/drawer-open? {;;:db/valueType :db.type/boolean
                          :db/cardinality :db.cardinality/one
                          :db/doc "The current state of the drawer"
                          } 
   :current/note-title {;;:db/valueType :db.type/string
                        :db/cardinality :db.cardinality/one
                        :db/doc "The current note's title"
                        } 
   :current/note-body {;;:db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "The current note's body"
                       } 
   :current/note-author {:db/valueType :db.type/ref
                         :db/cardinality :db.cardinality/one
                         :db/doc "The current note's author"
                         }} )

;; Define datoms to transact
(def datoms [{:current/entity  "current" 
              :current/drawer-open? false
              }
             {:db/id -2
              :note/title "Auto-TÜV" 
              :note/body "Spätestens am 01.08.2018"
              :note/author -7
              :note/tags ["GTD" "Termin" "Auto"]
              }
             {:db/id -3
              :note/title "Schulbücher kaufen" 
              :note/body "Englisch Klasse 9 ISBN 234234-23423-4-3424-23"
              :note/author -5
              :note/tags ["GTD" "Schule"]}
             {:db/id -4
              :note/title "Stammtisch" 
              :note/body "Termin find: http:/doodle.de/"
              :note/author -6
              :note/tags ["GTD" "Termin"]
              }
             {:db/id -5
              :person/name      "Tim"
              :person/login     "tim"
              :person/date      #inst "2016-04-08T22:00:00.000-00:00"}
             {:db/id -6
              :person/name      "Petra"
              :person/login     "petra"
              :person/date      #inst "2016-04-08T22:00:00.000-00:00"}
             {:db/id -7 
              :person/name      "Michael"
              :person/login     "michael"
              :person/date      #inst "2016-04-08T22:00:00.000-00:00"}
             ])

;;; Add the datoms via transaction
#_(d/transact! conn datoms)
#_(def conn  (d/init-db datoms schema))


(def conn  (d/create-conn schema))
#_(d/transact! conn datoms)
(connect! conn)

#_(defonce init-state
  {:current-page nil
   :route-params nil
   :drawer-open? false
   :note/list [[:note/by-id 1]
               [:note/by-id 2]
               [:note/by-id 3]]
   :note/current {:db/id -1
                  :note/title "" 
                  :note/body ""
                  :note/author nil}
   :person/list     [[:person/by-id 1]
                     [:person/by-id 2]
                     [:person/by-id 3]]
   :person/by-id    {1 {:db/id            1
                        :person/name      "Tim"
                        :person/login     "tim"
                        :person/date      #inst "2016-04-08T22:00:00.000-00:00"}
                     2 {:db/id            2
                        :person/name      "Petra"
                        :person/login     "petra"
                        :person/date      #inst "2016-04-08T22:00:00.000-00:00"}
                     3 {:db/id            3
                        :person/name      "Michael"
                        :person/login     "michael"
                        :person/date      #inst "2016-04-08T22:00:00.000-00:00"}}
   :person/new      {:person/name      ""
                     :person/date      nil}
   :note/by-id {1 {:db/id 1
                   :note/title "Auto-TÜV" 
                   :note/body "Spätestens am 01.08.2018"
                   :note/author [:person/by-id 3]
                   :note/tags ["GTD" "Termin" "Auto"]
                   }
                2 {:db/id 2
                   :note/title "Schulbücher kaufen" 
                   :note/body "Englisch Klasse 9 ISBN 234234-23423-4-3424-23"
                   :note/author [:person/by-id 1]
                   :note/tags ["GTD" "Schule"]}
                3 {:db/id 3
                   :note/title "Stammtisch" 
                   :note/body "Termin find: http:/doodle.de/"
                   :note/author [:person/by-id 2]
                   :note/tags ["GTD" "Termin"]
                   }}
   })
