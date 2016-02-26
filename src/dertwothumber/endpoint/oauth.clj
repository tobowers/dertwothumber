(ns dertwothumber.endpoint.oauth
  (:require [compojure.core :refer :all]
            [clj-http.client :as http])
  (:use [ring.middleware.session]
        [ring.util.response]
        [ring.util.codec]))

(defn- oauth2-params
  [config]
  {:client-id (:client-id config)
   :client-secret (:client-secret config)
   :authorize-uri  "https://github.com/login/oauth/authorize"
   :redirect-uri (str (:app-host config) "/oauth/github/authorize")
   :access-token-uri "https://github.com/login/oauth/access_token"
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
    (context "/oauth" []
      (GET "/github" []
        (let [config (:github-config config)]
          (println "/github oauth params: " config)
          (redirect (authorize-uri (oauth2-params config)))))
      (GET "/github/authorize" {params :params}
        (let [oauth2-params (oauth2-params (:github-config config))
              form-params {:code         (:code params)
                           :client_id    (:client-id oauth2-params)
                           :client_secret (:client-secret oauth2-params)
                           :redirect_uri (:redirect-uri oauth2-params)}]
          (println "form params: " form-params)
          (println "oauth2-params: " oauth2-params)
          (-> (http/post (:access-token-uri oauth2-params)
                         {:form-params form-params
                          :as :x-www-form-urlencoded})
              :body
              (println))))))
      (GET "/github/success" {params :params}
        (println params)
        (str "success" params))
