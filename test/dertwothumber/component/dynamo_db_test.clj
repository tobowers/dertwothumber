(ns dertwothumber.component.dynamo-db-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [dertwothumber.component.dynamo-db :as dynamo-db]))

(def test-config {:access-key "<AWS_DYNAMODB_ACCESS_KEY>"
                  :secret-key "<AWS_DYNAMODB_SECRET_KEY>"
                  :endpoint "http://dynamodb:8000"})

(deftest smoke-test
  (let [db (component/start (dynamo-db/dynamo-db-component test-config))]
    (testing "it can create a table"
      (dynamo-db/create-table db :bob [:id :n]))
    (testing "it can put an item and then get it back"
      (dynamo-db/create-table db :bob [:id :n])
      (dynamo-db/put-item db :bob {:id 0 :name "bobby"})
      (is (= "bobby" (:name (dynamo-db/get-item db :bob {:id 0})))))))
