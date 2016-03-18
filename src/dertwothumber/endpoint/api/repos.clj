(ns dertwothumber.endpoint.api.repos
  (:require [compojure.core :refer :all]
            [clj-http.client :as http]
            [tentacles.repos :as repos]
            [cheshire.core :refer :all]
            )
  (:use [ring.middleware.session]))

(defn repos-endpoint [config]
  (context "/api" []
    (GET "/repos" {session :session} []
      (let [user  (:user session)
            repos (repos/user-repos (:login user) {:all-pages true :oauth-token (:access-token session)})]
        (generate-string repos)))
    (PUT "/repos/:repo-name" {session :session {repo-name :repo-name} :params} []
      (let [user-id    (:login (:user session))
            url        (:app-host (:github-config config))
            event-list [:pull_request_review_comment :push :pull_request]]
        (repos/create-hook user-id repo-name "web"
                           {:url url}
                           {:active true
                            :events event-list
                            :oauth-token (:access-token session)})))))



;; (defn create-hook
;;   "Create a hook.
;;    Options are:
;;       events -- A sequence of event strings. Only 'push' by default.
;;       active -- true or false; determines if the hook is actually triggered
;;                 on pushes."
;;   [user repo name config options]


;; To create a webhook, the following fields are required by the config:

;; url: A required string defining the URL to which the payloads will be delivered.
;; content_type: An optional string defining the media type used to serialize the payloads. Supported values include json and form. The default is form.
;; secret: An optional string that's passed with the HTTP requests as an X-Hub-Signature header. The value of this header is computed as the HMAC hex digest of the body, using the secret as the key.
;; insecure_ssl: An optional string that determines whether the SSL certificate of the host for url will be verified when delivering payloads. Supported values include "0" (verification is performed) and "1" (verification is not performed). The default is "0".
