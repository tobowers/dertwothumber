(ns dertwothumber.endpoint.api.repos-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [dertwothumber.component.repo :refer [repo-component]
            [dertwothumber.endpoint.api.repos :refer [repos-endpoint]])
  (:use [peridot.core]))

(def repo-system
  (let [test-config {:access-key "<AWS_DYNAMODB_ACCESS_KEY>"
                     :secret-key "<AWS_DYNAMODB_SECRET_KEY>"
                     :endpoint "http://dynamodb:8000"}]
    (-> (component/system-map
          :repos (endpoint-component repos-endpoint)
          :dynamo-db test-config
          :repo-db (repo-component))
        (component/system-using
          {:repos [:github-config :repo-db]
           :dynamo-db []
           :repo-db [:dynamo-db]}))))

(deftest smoke-test
  (let [system (component/start repo-system)
        repos-endpoint (:repos system)]
    (testing "it list repos"
      (is (= 200 (-> (session repos-endpoint)
                     (request "/repos")
                     (:response)))))))

