(ns dertwothumber.component.user-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [dertwothumber.component.dynamo-db :as dynamo-db]
            [dertwothumber.component.user :as user]))

(def user-system
  (let [test-config {:access-key "<AWS_DYNAMODB_ACCESS_KEY>"
                     :secret-key "<AWS_DYNAMODB_SECRET_KEY>"
                     :endpoint "http://dynamodb:8000"}]
    (-> (component/system-map
          :db   (dynamo-db/dynamo-db-component test-config)
          :user (user/user-component))
        (component/system-using
          {:db []
           :user [:db]}))))

(deftest smoke-test
  (let [system (component/start user-system)
        db     (:db system)
        user   (:user system)]
    (user/create-table db)
    (testing "creating a user"
      (user/create-user user {:login "tobowers" :email "bob"}))
    (testing "fetching a user"
      (is (= "bob" (:email (user/get-user user "tobowers")))))))
