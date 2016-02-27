(ns dertwothumber.config
  (:require [environ.core :refer [env]]))

(def defaults
  ^:displace {:http {:port 3000}
              :app-host "http://localhost:3000"
              :auth {:secret "a replaceable secret that needs to be replaced"}})

(def environ
  {:http {:port (some-> env :port Integer.)}
   :github {:client-id (env :github-client-id) :client-secret (env :github-client-secret) :app-host (env :app-host)}
   :auth {:secret (some-> env :secret)}})
