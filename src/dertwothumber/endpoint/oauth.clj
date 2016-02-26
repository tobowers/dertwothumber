(ns dertwothumber.endpoint.oauth

  )

(defn oauth2-params
  [config]
  {:client-id (:github-client-id config)
   :client-secret (:github-client-secret config)
   :authorize-uri  "https://github.com/login/oauth/authorize"
   :redirect-uri (str (System/getenv "APP_HOST") "/connect/fitbit/success")
   :access-token-uri "https://api.fitbit.com/oauth2/token"
   :scope "activity profile"})


(defn oauth-endpoint [config]
  (context "/oauth" []
           (GET "/" {session :session}
                (let [count   (:count session 0)
                      session (assoc session :count (inc count))]
                  (-> (response (str "You accessed this page " count " times."))
                      (assoc :session session)
                      (content-type "text/plain "))))))

