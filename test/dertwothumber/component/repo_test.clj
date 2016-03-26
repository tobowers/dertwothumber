(ns dertwothumber.component.repo-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [dertwothumber.component.dynamo-db :as dynamo-db]
            [dertwothumber.component.repo :as repo]))

(def repo-system
  (let [test-config {:access-key "<AWS_DYNAMODB_ACCESS_KEY>"
                     :secret-key "<AWS_DYNAMODB_SECRET_KEY>"
                     :endpoint "http://dynamodb:8000"}]
    (-> (component/system-map
          :db   (dynamo-db/dynamo-db-component test-config)
          :repo (repo/repo-component))
        (component/system-using
          {:db []
           :repo [:db]}))))

(deftest smoke-test
  (let [system (component/start repo-system)
        db     (:db system)
        repo   (:repo system)]
    (repo/create-table db)
    (testing "creating a repo"
      (repo/create-repo repo {:full-name "tobowers/test-repo" :url "http://something"}))
    (testing "fetching a repo"
      (is (= "http://something" (:url (repo/get-repo repo "tobowers/test-repo")))))))
