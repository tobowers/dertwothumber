(ns dertwothumber.config
  (:require [environ.core :refer [env]]))

(def defaults
  ^:displace {:http {:port 3000}
              :app-host "http://localhost:3000"})

(def environ
  {:http {:port (some-> env :port Integer.)}
   :github {:client-id (env :github-client-id) :secret-key (env :github-secret-key) :app-host (env :app-host)}})
