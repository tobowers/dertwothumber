(ns dertwothumber.component.dynamo-db-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [dertwothumber.component.dynamo-db :as dynamo-db]))

(def test-config {:access-key "<AWS_DYNAMODB_ACCESS_KEY>"
                  :secret-key "<AWS_DYNAMODB_SECRET_KEY>"
                  :endpoint "http://dynamodb:8000"})

(deftest smoke-test
         (testing "it can create a table"
                  (let [db (component/start (dynamo-db/dynamo-db-component test-config))]
                    (dynamo-db/create-table db :bob [:id :n]))))

