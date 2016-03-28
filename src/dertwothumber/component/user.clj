(ns dertwothumber.component.user
  (:require [dertwothumber.component.dynamo-db :as dynamo-db]
            [com.stuartsierra.component :as component]))

(defprotocol UserFunctions
  (create-user [this attributes] "Create a user in the database")
  (get-user [this login] "find the user by login"))

(defrecord User []
  component/Lifecycle
  (start [component]
    component)
  (stop [component]
    component)

  UserFunctions
  (create-user [this attributes]
    (dynamo-db/put-item (:dynamo-db this) :users attributes))
  (get-user [this login]
    (dynamo-db/get-item (:dynamo-db this) :users {:login login})))

(defn create-table [dynamo-db]
  (dynamo-db/create-table dynamo-db :users [:login :s]))

(defn user-component []
  (map->User {}))