(ns dertwothumber.endpoint.api.repos
  (:require [compojure.core :refer :all]
            [clj-http.client :as http]
            [dertwothumber.component.repo :refer [list-repos create-hook]]
            [cheshire.core :refer :all])
  (:use [ring.middleware.session]))

(defn repos-endpoint [config]
  (let [repo-component (:repo config)]
    (context "/api" []
      (GET "/repos" {session :session} []
           (let [login  (:login (:user session))
                 access-token (:access-token session)
                 repos (list-repos repo-component login)]
             (generate-string repos)))
      (PUT "/repos/:repo-name" {session :session {repo-name :repo-name}
                                :params} []
           (let [user-id    (:login (:user session))]
             (create-hook repo-component user-id repo-name
                          (:access-token session)))))))
