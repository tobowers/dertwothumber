(ns dertwothumber.component.user-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [dertwothumber.component.dynamo-db :as dynamo-db]
            [dertwothumber.component.user :as user]))


(def test-config {:access-key "<AWS_DYNAMODB_ACCESS_KEY>"
                  :secret-key "<AWS_DYNAMODB_SECRET_KEY>"
                  :endpoint "http://dynamodb:8000"})

(deftest smoke-test
  (let [db   (component/start (dynamo-db/dynamo-db-component test-config))
        user (component/start (user/user-component db))]
    (user/create-table db)
    (testing "creating a user"
      (user/create-user user {:login "tobowers" :email "bob"}))
    (testing "fetching a user"
      (is (= "bob" (:email (user/get-user user "tobowers")))))))
