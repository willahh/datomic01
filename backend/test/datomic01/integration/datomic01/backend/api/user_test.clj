(ns datomic01.integration.datomic01.backend.api.user-test
  (:require [clojure.test :refer :all]
            [datomic01.backend.db.user :as db-user]))


(db-user/find-user-by-id db-user/user-datomic-repo 1)
