(ns dertwothumber.frontend.api.repos
  (:import goog.dom)
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.session :as session]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<! chan >!]]
            [cognitect.transit :as transit]))


(defn- csrf-token []
  (.getAttribute (goog.dom.getElement "csrf-token") "csrf-token"))

(defn fetch-repos []
  (let [response-channel (chan 1)
        reader (transit/reader :json)]
    (go
      (let [response (<! (http/get "/api/repos"))
            body     (:body response)]
       (>! response-channel (transit/read reader body))))
    response-channel))

(defn setup-repo [repo-name & headers]
  (go
    (http/put (str "/api/repos/" repo-name), {:default-headers {"x-csrf-token" (csrf-token)}})))
