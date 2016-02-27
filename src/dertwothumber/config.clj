(ns dertwothumber.config
  (:require [environ.core :refer [env]]
            [buddy.auth.backends.token :refer [jws-backend]]))

(def defaults
  ^:displace {:http {:port 3000}
              :app-host "http://localhost:3000"
              :authentication (jws-backend {:secret "a replaceable secret that needs to be replaced"})})

(def environ
  {:http {:port (some-> env :port Integer.)}
   :github {:client-id (env :github-client-id) :client-secret (env :github-client-secret) :app-host (env :app-host)}
   :authentication (jws-backend {:secret (some-> env :secret)})})
