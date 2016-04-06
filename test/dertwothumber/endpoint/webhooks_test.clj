(ns dertwothumber.endpoint.webhooks-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [dertwothumber.endpoint.webhooks :as webhooks]
            [ring.middleware.defaults :refer [wrap-defaults]]
            [dertwothumber.component.repo :refer [repo-component]]
            [cheshire.core :refer :all]))

(def handler
  (let [github-config {:client-id "client-id"
                       :client-secret "super secret"
                       :app-host "http://localhost"
                       :webhooks-url "http://localhost/webhooks"}
        repo-component (repo-component github-config)]
    (webhooks/webhooks-endpoint {:repo repo-component})))



(def test-pr-post-json
  (slurp (clojure.java.io/resource "test_pr_post.json")))

(deftest smoke-test
  (testing "can successfully post a PR"
    (let [endpoint handler
          request  (mock/request :post "/webhooks" {:payload test-pr-post-json})
          response (endpoint request)]
      (is (= 200 (:status response))))))


