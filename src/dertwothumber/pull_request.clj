(ns dertwothumber.pull-request
  (:require [tentacles.pulls]
            [tentacles.issues]
            [clojure.string]
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

(defn- post-pr-status [status-url state access-token]
  (api-call :post status-url nil {:state state
                                  :context "code-review"
                                  :description "awaiting thumbs"
                                  :oauth-token access-token}))

(defn- status-without-host [status-url]
  (clojure.string/replace status-url #"https://api.github.com" ""))

(defrecord PullRequest [access-token status-path user-id repo-name pr-id]
  GithubFunctions
  (get-comments [pull-request]
    (let [user-id (:user-id pull-request)
          repo-name (:repo-name pull-request)
          pr-id (:pr-id pull-request)
          access-token (:access-token pull-request)]
      (tentacles.issues/issue-comments user-id repo-name pr-id {:oauth-token access-token})))

  (set-state [pull-request state]
    {:pre [(contains? #{:pending :success :error :failure} state)]}
    (let [response (post-pr-status (:status-path pull-request) state (:access-token pull-request))]
      (println (str "setting " state " on " (:status-path pull-request) " response was: " response))))

  AcceptanceFunctions
  (is-accepted? [pull-request]
    (some comment-has-thumb? (get-comments pull-request)))

  (set-appropriate-state [pull-request]
    (if (is-accepted? pull-request)
      (set-state pull-request :success)
      (set-state pull-request :pending))))

(defn pull-request-event->PullRequest
  "see https://developer.github.com/v3/activity/events/types/#pullrequestevent for the data structure"
  [opts]
  (map->PullRequest {:user-id (get-in opts [:pull_request :base :repo :owner :login])
                     :repo-name (get-in opts [:pull_request :base :repo :name])
                     :pr-id (:number opts)
                     :access-token (:access-token opts)
                     :status-path (status-without-host (get-in opts [:pull_request :statuses_url]))}))


(defn issue-event->PullRequest [issue-payload]
  (let [pr-id        (get-in issue-payload [:issue :number])
        full-name    (clojure.string/split (get-in issue-payload [:repository :full_name]) #"/")
        user-id      (get full-name 0)
        repo-name    (get full-name 1)
        access-token (:access-token issue-payload)
        github-pr    (tentacles.pulls/specific-pull user-id repo-name pr-id {:oauth-token access-token})]
    (map->PullRequest {:access-token access-token
                       :pr-id pr-id
                       :user-id user-id
                       :repo-name repo-name
                       :status-path (status-without-host (:statuses_url github-pr))})))
