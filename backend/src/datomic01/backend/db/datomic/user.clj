(ns datomic01.backend.db.datomic.user
  (:require [datomic.api :as d]
            [datomic01.backend.db.datomic :as db-datomic :refer [connection-pool]]
            [mount.core :as mount]
            [datomic01.backend.db.utils :as db-utils]
            [datomic01.backend.model.user-model :as user-model]))

(defn find-list
  ([pool]
   (find-list pool '[:where [?e :user/id]]))
  ([pool where]
   (db-utils/find-where pool user-model/fields where))
  ([pool where & {:keys [offset limit order asc]}]
   (db-utils/find-where pool user-model/fields where
                        :offset offset
                        :limit limit
                        :order order
                        :asc asc)))

(defprotocol UserProtocol
  (find-user-by-id
    [repo user-id]
    "Récupère un utilisateur par user-id"))

(defrecord UserDatomicRepository
  [pool]
  UserProtocol
  (find-user-by-id
    [repo user-id]
    (find-list pool '[:where [?e :user/id user-id]])))

(mount/defstate user-datomic-repo
  :start (->UserDatomicRepository connection-pool))


(comment



  ;; Get user-id and user first-name by user-id
  (d/q '[:find ?e
         :in $ [?user-id]
         :where [?e :user/id ?user-id]]
       db [1])

  ;; Get all the fields where user-id = 1 : Method 1
  (let [entity-id (d/q '[:find ?e
                         :where [?e :user/id 1]]
                       db)
        entity (d/entity db (ffirst entity-id))]
    (d/touch entity))

  ;; Get all the fields where user-id = 1 : Method 2 with pull *
  (d/q '[:find (pull ?e [*])
         :in $ ?user-id
         :where [?e :user/id ?user-id]]
       db 1)

  ;; Get all the fields where user-id = 1 : Method 3 with pull and explicit fields
  ;; This method seems cleaner
  (d/q '[:find (pull ?e [:user/id :user/first-name :user/active :user/group :user/last-name])
         :in $ ?user-id
         :where [?e :user/id ?user-id]]
       db 1)

  ;; Get all the fields where user-id = 1 : Method 4 with pull and explicit fields and parameters
  ;; Cons : Parameters cannot be passed with this method (without using a macro)
  (let [user-id 1
        fields user-model/fields]
    (d/q '[:find (pull ?e [:user/id :user/first-name :user/active :user/group :user/last-name])
           :in $ ?user-id
           :where [?e :user/id ?user-id]]
         db 1))

  ;; Get all the fields where user-id = 1 : Method 5 with pull and explicit fields and parameters
  (d/query {:query '[:find (pull ?e fields)
                     :in $ ?user-id fields
                     :where [?e :user/id ?user-id]]
            :args  [db 1 [:user/id :user/first-name :user/active :user/group :user/last-name]]})







  (let [user-id 1
        fields user-model/fields]
    (d/q '{:find  [(pull ?e [:user/id :user/first-name :user/active :user/group :user/last-name])]
           :in    [$ ?user-id]
           :where [[?e :user/id ?user-id]]}
         db 1))


  (comment
    (query `[:find [[~'pull ~'?e ~fields] ...]]
           '[:in $]
           where)
    (d/q q db fields)

    (d/q `[:find (~'pull ~'?e ~[*])
           :in $ ~'?user-id
           :where [~'?e ~':user/id ~'?user-id]]
         db 1)


    (-> [{:a "1"}
         {:a "2"}
         {:a "3"}]
        conj)
    )





  (find-user-by-id user-datomic-repo 1)




  (find-list connection-pool '[:where [?e :user/id]])
  (find-list connection-pool '[:where [?e :user/id 1]])
  (find-list connection-pool)
  (find-list connection-pool '[:where [?e :user/id]])
  (find-list connection-pool '[:where [?e :user/id]] :offset 1 :limit 1)
  (find-list connection-pool '[:where [?e :user/id]] :offset 1)
  (find-list connection-pool '[:where [?e :user/id]] :limit 1)
  (find-list connection-pool '[:where [?e :user/id]]
             :order :user/first-name
             :asc true)
  (find-list connection-pool '[:where [?e :user/id]]
             :order :user/first-name
             :asc false)
  (find-list connection-pool '[:where [?e :user/id]]
             :order :user/first-name
             :asc false
             :limit 1)
  (find-list connection-pool '[:where [?e :user/id]]
             :order :user/first-name
             :asc false
             :offset 2)

  ;; Examples
  (db-utils/find-where connection-pool user-model/fields '[:where [?e :user/id]])
  (db-utils/find-where connection-pool user-model/fields '[:where [?e :user/id]] :limit 2 :offset 2)
  (db-utils/find-where connection-pool user-model/fields '[:where [?e :user/id]] :limit -1 :offset -1)
  (db-utils/find-where connection-pool '[*] '[:where [?e :keyword/id]] :limit 2)
  (db-utils/find-where connection-pool '[*] '[:where [?e :media/id]])
  (db-utils/find-where connection-pool '[*] '[:where [?e :user/id]])
  (db-utils/find-where connection-pool '[*] '[:where [?e :group/id]])
  (db-utils/find-where connection-pool '[:group/id :group/name] '[:where [?e :group/id 1]])
  (db-utils/find-where connection-pool '[*]
                       '[:where
                         [?e :user/id]
                         [?e :user/group 3]])

  (db-utils/find-where connection-pool '[*]
                       '[:where
                         [?e :media/id]
                         [?e :media/keyword 1]])

  (db-utils/find-where connection-pool user-model/fields
                       '[:where
                         [?e :user/id]
                         [?e :user/active true]])

  ;; Find user where first-name is "User 4"
  (db-utils/find-where connection-pool user-model/fields
                       '[:where
                         [?e :user/id]
                         [?e :user/first-name "User 4"]])

  ;; Find user by list of id with limit and offset
  (db-utils/find-where connection-pool user-model/fields
                       '[:where
                         (or [?e :user/id 1]
                             [?e :user/id 2]
                             [?e :user/id 3]
                             [?e :user/id 4])] :limit 2 :offset 1)


  (db-utils/find-where connection-pool '[:group/id :group/name]
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