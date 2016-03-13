(ns dertwothumber.endpoint.api.repos
  (:require [compojure.core :refer :all]
            [clj-http.client :as http]
            [tentacles.repos :as repos]
            [cheshire.core :refer :all])
  (:use [ring.middleware.session]))

(defn repos-endpoint [config]
  (context "/api" []
    (GET "/repos" {session :session} []
      (let [user  (:user session)
            repos (repos/user-repos (:login user) {:oauth-token (:access-token session)})]
        (generate-string repos)))))

