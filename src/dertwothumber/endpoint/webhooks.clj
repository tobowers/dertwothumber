(ns dertwothumber.endpoint.webhooks
  (:require [compojure.core :refer :all]
            [clj-http.client :as http]
            ;[tentacles.repos :as repos]
            [tentacles.pulls :as pulls]
            [cheshire.core :refer :all]
            [clojure.string :as str])
  (:use [ring.util.response]
        [clojure.walk]))

(defn- request->payload [request]
  (keywordize-keys (parse-string (:payload (:params request)))))

(defn- handle-pull-request [payload github-config]
  (let [full-name (str/split (get-in payload [:pull_request :base :repo :full_name]) #"/")
        pr-id     (get-in payload [:pull_request :id])
        user      (get full-name 0)
        repo      (get full-name 1)
        comments  (pulls/comments user repo pr-id {:client-id (:client-id github-config) :client-secret (:client-secret github-config)})]
    (println "handling a pull request")
    (println comments)))

(defn- is-pull-request [payload]
  (:pull_request payload))

(defn webhooks-endpoint [config]
  (context "/webhooks" []
    (POST "/" [:as request]
      (let [payload (request->payload request)
            github-config (:github-config config)]
        (cond
          (is-pull-request payload) (handle-pull-request payload github-config)
          :else (println payload))
        (response "OK")))))


      
