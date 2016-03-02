(ns dertwothumber.endpoint.api.repos
  (:require [compojure.core :refer :all]
            [clj-http.client :as http]
            [tentacles.repos :as repos])
  (:use [ring.middleware.session]))

(defn repos-endpont [config]
  (context "/api" []
    (GET "/repos" {session :session} []
      (let [user (:user session)]
        (repos/user-repos (:login user))))))

