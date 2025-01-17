(ns datomic01.model.user-dao
  (:require [datomic.api :as d]
            [datomic01.database :as database]
            [datomic01.model.user-model :as user-model]))

(defn find-list
  ([]
   (find-list '[:where [?e :user/id]]))
  ([where]
   (database/find-where user-model/fields where))
  ([where & {:keys [offset limit order asc]}]
   (database/find-where user-model/fields where
                        :offset offset
                        :limit limit
                        :order order
                        :asc asc)))
(comment
  (find-list)
  (find-list '[:where [?e :user/id]])
  (find-list '[:where [?e :user/id]] :offset 1 :limit 1)
  (find-list '[:where [?e :user/id]] :offset 1)
  (find-list '[:where [?e :user/id]] :limit 1)
  (find-list '[:where [?e :user/id]]
             :order :user/first-name
             :asc true)
  (find-list '[:where [?e :user/id]]
             :order :user/first-name
             :asc false)
  (find-list '[:where [?e :user/id]]
             :order :user/first-name
             :asc false
             :limit 1)
  (find-list '[:where [?e :user/id]]
             :order :user/first-name
             :asc false
             :offset 2)

  ;; Examples
  (database/find-where user-model/fields '[:where [?e :user/id]])
  (database/find-where user-model/fields '[:where [?e :user/id]] :limit 2 :offset 2)
  (database/find-where user-model/fields '[:where [?e :user/id]] :limit -1 :offset -1)
  (database/find-where '[*] '[:where [?e :keyword/id]] :limit 2)
  (database/find-where '[*] '[:where [?e :media/id]])
  (database/find-where '[*] '[:where [?e :user/id]])
  (database/find-where '[*] '[:where [?e :group/id]])
  (database/find-where '[:group/id :group/name] '[:where [?e :group/id 1]])
  (database/find-where '[*]
                       '[:where
                         [?e :user/id]
                         [?e :user/group 3]])

  (database/find-where '[*]
                       '[:where
                         [?e :media/id]
                         [?e :media/keyword 1]])

  (database/find-where user-model/fields
                       '[:where
                         [?e :user/id]
                         [?e :user/active true]])

  ;; Find user where first-name is "User 4"
  (database/find-where user-model/fields
                       '[:where
                         [?e :user/id]
                         [?e :user/first-name "User 4"]])

  ;; Find user by list of id with limit and offset
  (database/find-where user-model/fields
                       '[:where
                         (or [?e :user/id 1]
                             [?e :user/id 2]
                             [?e :user/id 3]
                             [?e :user/id 4])] :limit 2 :offset 1)


  (database/find-where '[:group/id :group/name]
                       '[:where
                         [?e :group/id]])

  ;; ;; Todo LIKE
  ;; (d/q '[:find ?entity ?name ?tx ?score
  ;;        :in $ ?search
  ;;        :where
  ;;        ;; [?e :user/first-name ?firstname]
  ;;        [(fulltext $ :user/first-name ?search) [[?entity ?name ?tx ?score]]]
  ;;        ]
  ;;      (database/get-db) "ll")

  ;; (d/q '[:find ?firstname
  ;;        :in $ ?search
  ;;        :where
  ;;        ;; [?e :user/id]
  ;;        [?e :user/first-name ?firstname]

  ;;        ]
  ;;      (database/get-db) "ll")
  )