(ns dertwothumber.endpoint.webhooks
  (:require [compojure.core :refer :all]
            [clj-http.client :as http]
            [tentacles.repos :as repos]
            [cheshire.core :refer :all])
  (:use [ring.util.response]
        [clojure.walk]))

(defn- request->payload [request]
  (keywordize-keys (parse-string (:payload (:params request)))))

(defn webhooks-endpoint [config]
  (context "/webhooks" []
    (POST "/" [:as request]
      (println (:zen (request->payload request)))
      (response "OK"))))
      
