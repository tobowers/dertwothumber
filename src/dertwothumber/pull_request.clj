(ns dertwothumber.pull-request
  (:require [tentacles.pulls]
            [tentacles.issues]
            [tentacles.core :refer [api-call]]))

(defprotocol GithubFunctions
  (get-comments [pull-request] "fetch the comments for a pull-request (for some reason this uses the issue API)")
  (set-status [pull-request status] "set the status of a pull-request "))

; see https://developer.github.com/v3/activity/events/types/#pullrequestevent for the data structure

(defrecord PullRequest [access-token]
  GithubFunctions
  (get-comments [pull-request]
    (let [user-id (get-in pull-request [:pull_request :base :repo :owner :login])
          repo-name (get-in pull-request [:pull_request :base :repo :name])
          pr-id (:number pull-request)
          access-token (:access-token pull-request)]
      (tentacles.issues/issue-comments user-id repo-name pr-id {:oauth-token access-token}))))

(defn pull-request [opts]
  (map->PullRequest opts))