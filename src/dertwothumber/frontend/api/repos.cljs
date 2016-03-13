(ns dertwothumber.frontend.api.repos
  (:require [reagent.session :as session]
            [cljs-http.client :as http]))


(defn fetch-repos []
  (http/get "/api/repos"))
