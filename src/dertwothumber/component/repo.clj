(ns dertwothumber.component.repo
  (:require [dertwothumber.component.dynamo-db :as dynamo-db]
            [com.stuartsierra.component :as component]))

(defprotocol RepoFunctions
  (create-repo [this attributes] "Create a repo in the database")
  (get-repo [this full-name] "get repo by full-name"))

(defrecord Repo [db]
  component/Lifecycle
  (start [component]
    (if-not (:db component)
      (assoc component :db db)
      component))
  (stop [component]
    (dissoc component :db))

  RepoFunctions
  (create-repo [this attributes]
    (dynamo-db/put-item (:db this) :repos attributes))
  (get-repo [this full-name]
    (dynamo-db/get-item (:db this) :repos {:full-name full-name})))

(defn create-table [db]
  (dynamo-db/create-table db :repos [:full-name :s]))

(defn repo-component []
  (Repo. {}))