(ns datomic01.database
  (:require [datomic.api :as d]
            [clojure.edn :as edn]))

(def uri "datomic:free://localhost:4334/example")
(def conn (d/connect uri))
(defn get-db []
  (d/db conn))
