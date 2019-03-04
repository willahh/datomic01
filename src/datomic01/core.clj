(ns datomic01.core
  (:require [datomic.api :as d]
            [clojure.edn :as edn]))

(def uri "datomic:free://localhost:4334/example")

(d/create-database uri)
(def conn (d/connect uri))


(d/transact conn (edn/read-string (slurp "resources/user_schema.edn")))
(d/transact conn (edn/read-string (slurp "resources/group_schema.edn")))
(d/transact conn (edn/read-string (slurp "resources/data/user.edn")))
(d/transact conn (edn/read-string (slurp "resources/data/group.edn")))

(d/transact conn [{:user/id 1 :user/group 1}])
(d/transact conn [{:user/id 2 :user/group 3}])
(d/transact conn [{:user/id 3 :user/group 3}])
(d/transact conn [{:user/id 4 :user/group 3}])

(let [db (d/db conn)]
  (d/q '[:find ?e
         :where
         [?e :user/first-name]] db))



(def db (d/db conn))
(def user-1 (d/entity db 17592186045425))

(let [db (d/entity-db user-1)]
  (d/q '[:find ?e
         :where
         [?e :user/id]]
       db))