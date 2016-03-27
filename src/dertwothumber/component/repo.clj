(ns dertwothumber.component.repo
  (:require [dertwothumber.component.dynamo-db :as dynamo-db]
            [com.stuartsierra.component :as component]
            [tentacles.repos]))

(defprotocol DbFunctions
  (create-repo [this attributes] "Create a repo in the database")
  (get-repo [this full-name] "get repo by full-name"))

(defprotocol GithubFunctions
  (list-repos [this login access-token])
  (create-hook [this user-id repo-name access-token]))

(defrecord Repo []
  component/Lifecycle
  (start [component]
    component)
  (stop [component]
    component)

  DbFunctions
  (create-repo [this attributes]
    (dynamo-db/put-item (:dynamo-db this) :repos attributes))
  (get-repo [this full-name]
    (dynamo-db/get-item (:dynamo-db this) :repos {:full-name full-name}))

  GithubFunctions
  (list-repos [_this login access-token]
    (tentacles.repos/user-repos login
                                {:all-pages true
                                 :oauth-token access-token}))

  (create-hook [this user-id repo-name access-token]
    (let [github-config (:github-config this)]
      (tentacles.repos/create-hook user-id repo-name "web"
                                   {:url (:webhooks-url github-config)}
                                   {:active true
                                    :events [:pull_request_review_comment :push
                                             :pull_request]
                                    :oauth-token access-token}))))

(defn create-table [dynamo-db]
  (dynamo-db/create-table dynamo-db :repos [:full-name :s]))

(defn repo-component [github-config]
  (map->Repo {:github-config github-config}))
