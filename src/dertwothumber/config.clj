(ns dertwothumber.config
  (:require [environ.core :refer [env]]
            [buddy.auth.backends.session :refer [session-backend]]))

(def defaults
  ^:displace {:http {:port 3000}
              :app-host "http://localhost:3000"
              :authentication (session-backend)})

(def environ
  {:http {:port (some-> env :port Integer.)}
   :github {:client-id (env :github-client-id) :client-secret (env :github-client-secret) :app-host (env :app-host)}
   :authentication (session-backend)})
