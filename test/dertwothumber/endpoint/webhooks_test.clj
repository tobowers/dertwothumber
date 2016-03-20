(ns dertwothumber.endpoint.webhooks-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [dertwothumber.endpoint.webhooks :as webhooks]
            [ring.middleware.defaults :refer [wrap-defaults]]
            [cheshire.core :refer :all]
            [ring.util.codec :as codec]))

(def handler
  (->
    (webhooks/webhooks-endpoint {:github-config {:client-id "test-client-id"
                                                 :client-secret "test-client-secret"
                                                 :app-host "http://fake-host"}})
    (wrap-defaults {:params {:urlencoded true
                             :multipart  true
                             :nested     true
                             :keywordize true}})))

(def test-pr-post-json
  (slurp (clojure.java.io/resource "test_pr_post.json")))

(deftest smoke-test
  (testing "can successfully post a PR"
    (let [request  (mock/request :post "/webhooks" {:payload test-pr-post-json})
          response (handler request)]
      (is (= 200 (:status response))))))


