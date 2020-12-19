(ns datomic01.core-test
  (:require [clojure.test :refer :all]
            [datomic01.core :refer :all]
            [testit.core :refer :all]))

(deftest midje-impersonation
  (facts
    (+ 1 2) => 3
    {:a 1 :z 1} =in=> {:a 1}))
