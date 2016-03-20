(ns dertwothumber.endpoint.oauth-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [dertwothumber.endpoint.oauth :as oauth]
            [cheshire.core :refer :all])
  (:use [clj-http.fake]))

(def handler
  (oauth/oauth-endpoint {:github-config {:client-id "test-client-id" :client-secret "test-client-secret" :app-host "http://fake-host"}}))

(deftest smoke-test
  (testing "oauth page exists"
    (let [response (handler (mock/request :get "/oauth/github"))]
      (is (= 302 (:status response)))
      (is (= "https://github.com/login/oauth/authorize?response_type=code&client_id=test-client-id&redirect_uri=http%3A%2F%2Ffake-host%2Foauth%2Fgithub%2Fauthorize&scope=repo%2Cadmin%3Arepo_hook"
             (get (:headers response) "Location")))))

  (testing "handling github response"
    (with-fake-routes {"https://github.com/login/oauth/access_token" {:post (fn [req] {:status 200 :headers {} :body "access_token=1234"})}
                       "https://api.github.com/user?access_token=1234" {:get (fn [req] {:status 200 :headers {} :body (generate-string {:login "bob"})})}}
      (let [response (handler (mock/request :get "/oauth/github/authorize"))]
        (is (= 302 (:status response)))
        (is (= "1234" (-> (:session response)
                          :access-token)))
        (is (= "bob") (-> (:session response)
                          :user
                          :login)))))

  (testing "logging out"
    (let [response (handler (mock/request :get "/oauth/logout" {:session {:user "bob"}}))]
      (is (= 302 (:status response)))
      (is (= nil (:session response))))))

