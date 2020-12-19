(ns datomic01.backend.db.datomic
  (:require [datomic.api :as d]
            [mount.core :as mount]))

(mount/defstate conn
          :start (do
                   (def uri "datomic:free://localhost:4334/example")
                   (d/create-database uri)
                   (d/connect uri))
          :stop (fn [] (prn "TODO: Stop datomic connection pool")))