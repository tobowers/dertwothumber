(ns dertwothumber.component.repo-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [dertwothumber.component.dynamo-db :as dynamo-db]
            [dertwothumber.component.repo :as repo]))

(def repo-system
  (let [test-config {:access-key "<AWS_DYNAMODB_ACCESS_KEY>"
                     :secret-key "<AWS_DYNAMODB_SECRET_KEY>"
                     :endpoint "http://dynamodb:8000"}
        github-config {:client-id "client-id"
                       :client-secret "super secret"
                       :app-host "http://localhost"
                       :webhooks-url "http://localhost/webhooks"}]

    (-> (component/system-map
          :dynamo-db   (dynamo-db/dynamo-db-component test-config)
          :repo (repo/repo-component github-config))
        (component/system-using
          {:dynamo-db []
           :repo [:dynamo-db]}))))

(deftest smoke-test
  (let [system (component/start repo-system)
        dynamo-db (:dynamo-db system)
        repo   (:repo system)]
    (repo/create-table dynamo-db)
    (testing "creating a repo"
      (repo/create-repo repo {:full-name "tobowers/test-repo" :url "http://something"}))
    (testing "fetching a repo"
      (is (= "http://something" (:url (repo/get-repo repo "tobowers/test-repo")))))))
