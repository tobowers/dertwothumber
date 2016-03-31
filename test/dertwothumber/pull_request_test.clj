(ns dertwothumber.pull-request-test
  (:require [clojure.test :refer :all]
            [dertwothumber.pull-request :refer [pull-request get-comments]]
            [conjure.core :refer [mocking verify-call-times-for verify-first-call-args-for]]
            [cheshire.core :refer :all]
            [tentacles.pulls]
            [tentacles.issues]))

(def test-pr-post
  (parse-string (slurp (clojure.java.io/resource "test_pr_post.json")) true))

(deftest testing-pull-requests
  (testing "it fetches comments"
    (let [pr (pull-request (assoc test-pr-post :access-token "abc"))]
      (mocking [tentacles.issues/issue-comments]
        (get-comments pr)
        (verify-call-times-for tentacles.issues/issue-comments 1)))))
