(ns dertwothumber.endpoint.example
  (:require [compojure.core :refer :all]
            [clojure.java.io :as io])
  (:use [ring.middleware.session]
        [ring.util.response]))

(defn example-endpoint [config]
  (context "/example" []
    (GET "/" {session :session}
      (let [count   (:count session 0)
            session (assoc session :count (inc count))]
        (-> (response (str "You accessed this page " count " times."))
            (assoc :session session)
            (content-type "text/plain "))))))