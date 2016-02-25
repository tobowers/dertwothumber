(ns dertwothumber.endpoint.example
  (:require [compojure.core :refer :all]
            [clojure.java.io :as io]))

(defn example-endpoint [config]
  (context "/example" []
    (GET "/" []
      (io/resource "dertwothumber/endpoint/example/example.html"))))
