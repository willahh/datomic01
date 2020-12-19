(ns user
  (:require [mount.core :as mount]
            [clojure.tools.namespace.repl :as tnr]))

(defn go []
  (mount/start)
  :ready)

(defn reset []
  (mount/stop)
  (tnr/refresh :after 'dev/go))

(comment
  (go)
  (mount/stop)
  (reset)
  )