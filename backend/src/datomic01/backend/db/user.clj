(ns datomic01.backend.db.user
  (:require [datomic.api :as d]
            [mount.core :as mount]
            [datomic01.backend.db.utils :as db-utils]
            [datomic01.backend.model.user-model :as user-model]))

(defn find-list
  ([]
   (find-list '[:where [?e :user/id]]))
  ([where]
   (db-utils/find-where user-model/fields where))
  ([where & {:keys [offset limit order asc]}]
   (db-utils/find-where user-model/fields where
                        :offset offset
                        :limit limit
                        :order order
                        :asc asc)))

(defprotocol UserProtocol
  (find-user-by-id
    [repo user-id]
    "Récupère un utilisateur par user-id"))

(defrecord UserRepository
  [pool]
  UserProtocol
  (find-user-by-id
    [_ user-id]
    (find-list '[:where [?e :user/id]])))

(mount/defstate user-repo
  :start (->UserRepository nil))

(comment


  (find-user-by-id user-repo 1)

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
  (db-utils/find-where user-model/fields '[:where [?e :user/id]])
  (db-utils/find-where user-model/fields '[:where [?e :user/id]] :limit 2 :offset 2)
  (db-utils/find-where user-model/fields '[:where [?e :user/id]] :limit -1 :offset -1)
  (db-utils/find-where '[*] '[:where [?e :keyword/id]] :limit 2)
  (db-utils/find-where '[*] '[:where [?e :media/id]])
  (db-utils/find-where '[*] '[:where [?e :user/id]])
  (db-utils/find-where '[*] '[:where [?e :group/id]])
  (db-utils/find-where '[:group/id :group/name] '[:where [?e :group/id 1]])
  (db-utils/find-where '[*]
                       '[:where
                         [?e :user/id]
                         [?e :user/group 3]])

  (db-utils/find-where '[*]
                       '[:where
                         [?e :media/id]
                         [?e :media/keyword 1]])

  (db-utils/find-where user-model/fields
                       '[:where
                         [?e :user/id]
                         [?e :user/active true]])

  ;; Find user where first-name is "User 4"
  (db-utils/find-where user-model/fields
                       '[:where
                         [?e :user/id]
                         [?e :user/first-name "User 4"]])

  ;; Find user by list of id with limit and offset
  (db-utils/find-where user-model/fields
                       '[:where
                         (or [?e :user/id 1]
                             [?e :user/id 2]
                             [?e :user/id 3]
                             [?e :user/id 4])] :limit 2 :offset 1)


  (db-utils/find-where '[:group/id :group/name]
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