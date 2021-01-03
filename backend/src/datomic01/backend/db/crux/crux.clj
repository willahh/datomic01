(ns datomic01.backend.db.crux.crux
  (:require [crux.api :as crux]
            [clojure.java.io :as io]
            [mount.core :as mount]))

(mount/defstate connection-pool
  :start (let [node (crux/start-node
                      {:crux/index-store {:kv-store {:crux/module 'crux.lmdb/->kv-store
                                                     :db-dir      (io/file "resources/data/lmdb")}}})]
           node)
  :stop (fn []
          (prn "TODO connection pool")))