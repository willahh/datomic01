(ns datomic01.model.user-dao
  (:require [datomic.api :as d]
            [datomic01.database :as database]
            [datomic01.model.user-model :as user-model]))

(def db (database/get-db))

(defn query [find in where]
  (reduce #(apply conj %1 %2)
          (conj [] find in where)))

(defn find-fields-where [db fields where]
  (let [q (query `[:find [[~'pull ~'?e ~fields] ...]]
                 '[:in $]
                 where)]
    (d/q q db user-model/fields)))


;; Examples
(find-fields-where (database/get-db) user-model/fields '[:where [?e :user/id]])
(find-fields-where (database/get-db) '[*] '[:where [?e :keyword/id]])
(find-fields-where (database/get-db) '[*] '[:where [?e :media/id]])
(find-fields-where (database/get-db) '[*] '[:where [?e :user/id]])
(find-fields-where (database/get-db) '[*] '[:where [?e :group/id]])
(find-fields-where (database/get-db) '[:group/id :group/name] '[:where [?e :group/id 1]])
(find-fields-where (database/get-db)
                   '[*]
                   '[:where
                     [?e :user/id]
                     [?e :user/group 3]])
(find-fields-where (database/get-db)
                   '[*]
                   '[:where
                     [?e :media/id]
                     [?e :media/keyword 1]])
(find-fields-where (database/get-db)
                   user-model/fields
                   '[:where
                     [?e :user/id]
                     [?e :user/active true]])
(find-fields-where (database/get-db)
                   user-model/fields
                   '[:where
                     [?e :user/id]
                     [?e :user/first-name "User 4"]])

;; Todo LIKE
(d/q '[:find ?entity ?name ?tx ?score
       :in $ ?search
       :where
       ;; [?e :user/first-name ?firstname]
       [(fulltext $ :user/first-name ?search) [[?entity ?name ?tx ?score]]]
       ]
     (database/get-db) "ll")

(d/q '[:find ?firstname
       :in $ ?search
       :where
       ;; [?e :user/id]
       [?e :user/first-name ?firstname]
       
       ]
     (database/get-db) "ll")

(comment
  ;; Todo add limit
  (d/q '[:find [[pull ?e [:user/id]]] 
         :where [?e :user/id]]
       (database/get-db))

  (d/q '[:find (pull ?e [:user/id])
         :where [?e :user/id]]
       (database/get-db))

  (d/q '[:find (pull ?e (:user/id))
         :where [?e :user/id]]
       (database/get-db))

  (d/q '[:find (pull ?e [:user/id (:user/id :limit 2)])
         :where [?e :user/id]]
       (database/get-db)))





(let [db (database/get-db)]
  (d/q '[:find ?e ?first-name
         :where
         [?e :user/first-name ?first-name]] db))

(let [e '[?e ...]
      db (database/get-db)
      fields user-model/fields
      q (d/q '[:find ?f
               :in $ ?f
               :where
               [?e :user/id]] db e)]
  (d/pull db fields 17592186045419))

(let [db (database/get-db)
      fields user-model/fields
      q '[:find [[pull ?e [:user/id :user/first-name :user/last-name]] ...]
          :in $
          :where
          [?e :user/id]]]
  (d/q q db user-model/fields))

(let [db (database/get-db)
      fields user-model/fields
      q '[:find [[pull ?e [:user/id :user/first-name :user/last-name]] ...]
          :in $
          :where
          [?e :user/id]]]
  (d/q q db user-model/fields))

(let [db (database/get-db)
      fields user-model/fields
      q '[:find [[pull ?e [:user/id :user/first-name :user/last-name]] ...]
          :where [?e :user/id]
          :in $]]
  (d/q q db user-model/fields))

(let [db (database/get-db)
      find '[:find [[pull ?e [:user/id :user/first-name :user/last-name]] ...]]
      where '[:where
              [?e :user/id]]
      in '[:in $]
      q (apply conj [find find in in where in])]
  ;; (d/q q db)
  q  )





(let [db (database/get-db)]
  (d/q '[:find [[pull ?e [:group/id :group/name]] ...]
         :where
         [?e :group/id]] db))

(let [db (database/get-db)]
  (d/q '[:find ?e
         :where
         [?e :group/id]] db))



(defn find-by-id [user-id]
  )
