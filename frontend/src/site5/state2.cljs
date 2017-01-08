(ns site5.state2
  (:require  [com.stuartsierra.component :as component]))

(defonce init-state
  {:note/list [[:note/by-id 1]
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
                3 {:db/id 2
                   :note/title "Stammtisch" 
                   :note/body "Termin find: http:/doodle.de/"
                   :note/author [:person/by-id 2]
                   :note/tags ["GTD" "Termin"]
                   }}
   })


(defrecord State []
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (println "Starting State")
    (assoc this :app-state (atom init-state)))

  (stop [this]
    (println "Stopping State!")
    (dissoc this :app-state)))

(defn new-state []
  (map->State {}))
