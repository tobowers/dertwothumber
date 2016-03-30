(ns dertwothumber.endpoint.webhooks
  (:require [compojure.core :refer :all]
            [clj-http.client :as http]
            [dertwothumber.component.repo :refer [get-repo]]
            [dertwothumber.pull-request :refer [pull-request get-comments]]
            [cheshire.core :refer :all]
            [clojure.string :as str])
  (:use [ring.util.response]
        [clojure.walk]))

(defn- request->payload [request]
  (keywordize-keys (parse-string (:payload (:params request)))))

(defn- handle-pull-request [payload repo-component]
  (let [full-name   (str/split (get-in payload [:pull_request :base :repo :full_name]) #"/")
        pr-id       (:number payload)
        user-id     (get full-name 0)
        repo-name   (get full-name 1)
        stored-repo (get-repo repo-component (str user-id "/" repo-name))
        pr          (pull-request {:user-id user-id :repo-name repo-name :pr-id pr-id :access-token (:access-token stored-repo)})
        comments    (get-comments pr)]
    (println "handling a pull request with comments: " comments)))

(defn- is-pull-request [payload]
  (:pull_request payload))

(defn webhooks-endpoint [config]
  (context "/webhooks" []
    (POST "/" [:as request]
      (let [payload (request->payload request)
            repo-component (:repo config)]
        (cond
          (is-pull-request payload) (handle-pull-request payload repo-component)
          :else (println "not a pull request: " payload))
        (response "OK")))))


      
