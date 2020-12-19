(ns datomic01.backend.db.utils
  (:require [datomic.api :as d]
            [datomic01.backend.db.datomic :as datomic]
            [mount.core :as mount]))

(defn get-db []
  (d/db datomic/conn))

(defn query [find in where]
  (reduce #(apply conj %1 %2)
          (conj [] find in where)))

(defn find-where [fields where & {:keys [db offset limit order asc]}]
  (let [q (query `[:find [[~'pull ~'?e ~fields] ...]]
                 '[:in $]
                 where)
        db (if db db (get-db))
        result (d/q q db fields)]
    (if limit
      (take limit
            (if offset
              (drop offset result)
              result))
      (if offset
        (drop offset result)
        result))))
