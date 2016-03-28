(ns dertwothumber.component.dynamo-db
  (:require [taoensso.faraday :as far]
            [com.stuartsierra.component :as component]))

(defprotocol DatabaseFunctions
  (-create-table [this table-name hash-keydef opts] "the name of the table to create")
  (put-item [component table item] "the keys to put")
  (get-item [component table lookup-hash] "get an item from the id")
  (freeze [attribute-map] "pass in a map, get the faraday frozen"))

(defrecord DynamoDb []
  component/Lifecycle
  (start [component]
    component)
  (stop [component]
    component)

  DatabaseFunctions
  (-create-table [this table-name hash-keydef opts]
    (far/ensure-table (:client-ops this) table-name hash-keydef opts))
  (freeze [attribute-map]
    (far/freeze attribute-map))
  (put-item [this table item]
    (far/put-item (:client-ops this) table item))
  (get-item [this table lookup-hash]
    (far/get-item (:client-ops this) table lookup-hash)))


(defn create-table [dynamodb table-name hash-keydef & opts]
  (-create-table dynamodb table-name hash-keydef opts))

(defn dynamo-db-component [config]
  (map->DynamoDb {:client-ops config}))