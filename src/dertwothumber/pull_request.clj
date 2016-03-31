(ns dertwothumber.pull-request
  (:require [tentacles.pulls]
            [tentacles.issues]
            [tentacles.core :refer [api-call]]))

(def thumbs-up-regex #"\:\+1\:")

(defprotocol GithubFunctions
  (get-comments [pull-request] "fetch the comments for a pull-request (for some reason this uses the issue API)")
  (set-state [pull-request state] "set the status of a pull-request "))

(defprotocol AcceptanceFunctions
  (is-accepted? [pull-request] "if the pull request passes dertwothumber this is true")
  (set-appropriate-state [pull-request] "if PR has a thumb then :success else :pending"))

(defn- comment-has-thumb? [comment]
  (re-find thumbs-up-regex (:body comment)))

; see https://developer.github.com/v3/activity/events/types/#pullrequestevent for the data structure

(defrecord PullRequest [access-token]
  GithubFunctions
  (get-comments [pull-request]
    (let [user-id (get-in pull-request [:pull_request :base :repo :owner :login])
          repo-name (get-in pull-request [:pull_request :base :repo :name])
          pr-id (:number pull-request)
          access-token (:access-token pull-request)]
      (tentacles.issues/issue-comments user-id repo-name pr-id {:oauth-token access-token})))

  (set-state [pull-request state]
    {:pre [(contains? #{:pending :success :error :failure} state)]}
    (let [status-url (get-in pull-request [:pull_request :statuses_url])]
      (api-call :post status-url {:state state
                                  :context "code-review"
                                  :description "awaiting thumbs"})))
  AcceptanceFunctions
  (is-accepted? [pull-request]
    (some comment-has-thumb? (get-comments pull-request)))

  (set-appropriate-state [pull-request]
    (if (is-accepted? pull-request)
      (set-state pull-request :success)
      (set-state pull-request :pending))))

(defn pull-request [opts]
  (map->PullRequest opts))
