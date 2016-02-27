(ns dertwothumber.endpoint.oauth-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [dertwothumber.endpoint.oauth :as oauth]
            )
  (:use [clj-http.fake]))

(def handler
  (oauth/oauth-endpoint {:github-config {:client-id "test-client-id" :client-secret "test-client-secret" :app-host "http://fake-host"}}))

(deftest smoke-test
  (testing "oauth page exists"
    (let [response (handler (mock/request :get "/oauth/github"))]
      (is (= 302 (:status response)))
      (is (= "https://github.com/login/oauth/authorize?response_type=code&client_id=test-client-id&redirect_uri=http%3A%2F%2Ffake-host%2Foauth%2Fgithub%2Fauthorize&scope=activity%20profile"
             (get (:headers response) "Location")))
      ))
  (testing "handling github response"
    (with-fake-routes {"https://github.com/login/oauth/access_token" {:post (fn [req] {:status 200 :headers {} :body "code=1234"})}}
      (let [response (handler (mock/request :get "/oauth/github/authorize" ))]
        (is (= 200 (:status response)))
      ))))
