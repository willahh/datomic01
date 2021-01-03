(ns add-data-for-crux
  (:require [clojure.edn :as edn]
            [crux.api :as api]
            [datomic01.backend.db.crux.crux :refer [connection-pool]]))

(defn add-data-for-edn-sample
  [edn-file-path field-id]
  (api/submit-tx
    connection-pool
    (let [rows (edn/read-string (slurp edn-file-path))]
      (mapv (fn [row]
              (let [crux-id (keyword (str (-> (str field-id)
                                              (clojure.string/replace #"/" ".")
                                              (clojure.string/replace #":" "")) "." (field-id row)))]
                [:crux.tx/put (assoc row
                                :crux.db/id crux-id)]))
            rows))))

(defn add-data []
  (add-data-for-edn-sample "resources/data/company.edn" :company/id)
  (add-data-for-edn-sample "resources/data/group.edn" :group/id)
  (add-data-for-edn-sample "resources/data/keyword.edn" :keyword/id)
  (add-data-for-edn-sample "resources/data/media.edn" :media/id)
  (add-data-for-edn-sample "resources/data/profile.edn" :profile/id)
  (add-data-for-edn-sample "resources/data/user.edn" :user/id))

(comment
  ;; Add data samples
  (add-data)

  ;; Find * in user
  (api/q (api/db connection-pool)
         '{:find          [e first-name]
           :where         [[e :user/first-name first-name]]
           :full-results? true}))