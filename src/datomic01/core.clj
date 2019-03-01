(ns datomic01.core
  (:require [datomic.api :as d]
            [clojure.edn :as edn]))


(def uri "datomic:free://localhost:4334/example")

(d/create-database uri)
(def conn (d/connect uri))


(d/transact conn (edn/read-string (slurp "resources/user_schema.edn")))
(d/transact conn (edn/read-string (slurp "resources/data/user.edn")))

(let [db (d/db conn)]
  (d/q '[:find ?e
        :where
        [?e :user/first-name]] db))
