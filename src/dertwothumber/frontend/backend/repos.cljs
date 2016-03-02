(ns dertwothumber.frontend.backend.repos
  (:require [reagent.session :as session]
            [cljs-http :as http]
            [cljs.core.async :refer [<!]]))


(defn fetch-repos []
  (let [response (<! (http/get "/api/repos"))]
    (println response)))
