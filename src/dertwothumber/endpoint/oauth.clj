(ns dertwothumber.endpoint.oauth
  (:require [compojure.core :refer :all])
  (:use [ring.middleware.session]
        [ring.util.response]
        [ring.util.codec]))

(defn- oauth2-params
  [config]
  (println config)
  {:client-id (:client-id config)
   :client-secret (:client-secret config)
   :authorize-uri  "https://github.com/login/oauth/authorize"
   :redirect-uri (str (:app-host config) "/oauth/github/authorize")
   :access-token-uri "https://api.fitbit.com/oauth2/token"
   :scope "activity profile"})

(defn- authorize-uri [client-params]
  (str
    (:authorize-uri client-params)
    "?response_type=code"
    "&client_id="
    (url-encode (:client-id client-params))
    "&redirect_uri="
    (url-encode (:redirect-uri client-params))
    "&scope="
    (url-encode (:scope client-params))))


(defn oauth-endpoint [config]
  (let [config (:github-config config)]
    (context "/oauth" []
      (GET "/github" []
        (redirect (authorize-uri (oauth2-params config)))))))
