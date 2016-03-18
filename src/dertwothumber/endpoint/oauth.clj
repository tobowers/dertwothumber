(ns dertwothumber.endpoint.oauth
  (:require [compojure.core :refer :all]
            [clj-http.client :as http]
            [dertwothumber.static-view.loading-page :as loading-page])
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
   :scope "repo,admin:repo_hook"})

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

(defn- fetch-github-access-token
  [oauth2-params code]
  (-> (http/post (:access-token-uri oauth2-params)
                 {:form-params {:code         code
                                :client_id    (:client-id oauth2-params)
                                :client_secret (:client-secret oauth2-params)
                                :redirect_uri (:redirect-uri oauth2-params)}
                  :as :x-www-form-urlencoded})
      :body
      :access_token))

(defn- fetch-github-user [access-token]
  (-> (http/get "https://api.github.com/user" {:query-params {:access_token access-token}
                                               :as :json})
      :body))

(defn oauth-endpoint [config]
    (context "/oauth" []
      (GET "/github" []
        (let [config (:github-config config)]
          (redirect (authorize-uri (oauth2-params config)))))
      (GET "/github/authorize" {params :params session :session}
        (let [oauth2-params (oauth2-params (:github-config config))
              access-token  (fetch-github-access-token oauth2-params (:code params))
              user          (fetch-github-user access-token)
              session       (assoc session :access-token access-token :user user)]
           (-> (redirect "/ui")
               (assoc :session session))
         ))
      (GET "/logout" []
        (-> (redirect "/ui")
            (assoc :session nil)))))
