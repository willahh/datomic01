(ns datomic01.database
  (:require [datomic.api :as d]))

(def uri "datomic:free://localhost:4334/example")
(def conn (d/connect uri))
(defn get-db []
  (d/db conn))

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
