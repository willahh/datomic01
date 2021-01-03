(ns datomic01.backend.db.datomic.datomic
  (:require [datomic.api :as d]
            [mount.core :as mount]))

(mount/defstate connection-pool
          :start (do
                   (def uri "datomic:free://localhost:4334/example")
                   (d/create-database uri)
                   (d/connect uri))
          :stop (fn []
                  (prn "Stop datomic connection pool")
                  (d/shutdown true)))

(mount/defstate datomic-db
  :start (d/db connection-pool))

