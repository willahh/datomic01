(ns datomic01.backend.db.crux.user
  (:require [crux.api :as api]
            [mount.core :as mount]
            [datomic01.backend.db.crux.crux :refer [connection-pool]]
            [crux.api :as crux]))

(defprotocol UserProtocol
  (find-user-by-id
    [repo user-id]
    "Récupère un utilisateur par user-id"))

(defrecord UserCruxRepository
  [pool]
  UserProtocol
  (find-user-by-id
    [repo user-id]
    [{:todo "todo"}]))

(mount/defstate user-crux-repo
  :start (->UserCruxRepository connection-pool))


(comment

  ;; Queries ---------------------------------------------------------
  ;; Find * in user
  (api/q (api/db connection-pool)
         '{:find          [e first-name]
           :where         [[e :user/first-name first-name]]
           :full-results? true})

  ;; find * in user where user/id = 1
  (api/q (api/db connection-pool)
         '{:find          [e]
           :where         [[e :user/id id]]
           :args          [{:id 1}]
           :full-results? true})

  ;; find entity by id
  (api/entity (api/db connection-pool) :group.id.1)

  ;; find * in user where user-first-name = "William"
  (api/q (api/db connection-pool)
         '{:find          [e]
           :where         [[e :user/first-name user-first-name]]
           :args          [{:user-first-name "William"}]
           :full-results? true})

  ;; find * in group
  (api/q (api/db connection-pool)
         '{:find          [e name]
           :where         [[e :group/name name]]
           :full-results? true})


  ;; Updates  ---------------------------------------------------------
  (api/q (api/db connection-pool)
         '{:find          [e first-name]
           :where         [[e :group/id first-name]]
           :full-results? true})

  (api/entity (api/db connection-pool) :group.id.1)
  ;; => "Groupe 1"

  ;; Update group entity set group/name = "Groupe 1 updated" where group.id = 1
  (let [group (api/entity (api/db connection-pool) :group.id.1)]
    (api/submit-tx connection-pool
                   [[:crux.tx/put (assoc group
                                    :group/name "Groupe 1 updated")]]))
  ;; => "Groupe 1 updated"

  )