(ns dertwothumber.pull-request
  (:require [tentacles.pulls]))

(defprotocol GithubFunctions
  (get-comments [pull-request] "fetch the comments for a pull-request"))

(defrecord PullRequest [user-id repo-name pr-id access-token]
  GithubFunctions
  (get-comments [pull-request]
    (let [{user-id :user-id repo-name :repo-name pr-id :pr-id access-token :access-token} pull-request]
      (tentacles.pulls/comments user-id repo-name pr-id {:oauth-token access-token}))))

(defn pull-request [opts]
  (map->PullRequest opts))