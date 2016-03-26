(ns dertwothumber.component.user
  (:require [dertwothumber.component.dynamo-db :as dynamo-db]
            [com.stuartsierra.component :as component]))

(defprotocol UserFunctions
  (create-user [this attributes] "Create a user in the database")
  (get-user [this login] "find the user by login"))

(defrecord User [db]
  component/Lifecycle
  (start [component]
    (if-not (:db component)
      (assoc component :db db)
      component))
  (stop [component]
    (dissoc component :db))

  UserFunctions
  (create-user [this attributes]
    (dynamo-db/put-item (:db this) :users attributes))
  (get-user [this login]
    (dynamo-db/get-item (:db this) :users {:login login})))


(defn create-table [dynamodb]
  (dynamo-db/create-table dynamodb :users [:login :s]))

(defn user-component [dynamo-db]
  (User. dynamo-db))