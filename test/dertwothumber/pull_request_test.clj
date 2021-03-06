(ns dertwothumber.pull-request-test
  (:require [clojure.test :refer :all]
            [dertwothumber.pull-request :refer [pull-request-event->PullRequest
                                                set-appropriate-state
                                                get-comments
                                                is-accepted?
                                                set-state]]
            [conjure.core :refer [mocking stubbing verify-call-times-for verify-first-call-args-for]]
            [cheshire.core :refer :all]
            [tentacles.pulls]
            [tentacles.core]
            [tentacles.issues]))

(def test-pr-post
  (parse-string (slurp (clojure.java.io/resource "test_pr_post.json")) true))

(deftest acceptance-functions-tests
  (let [pr (pull-request-event->PullRequest (assoc test-pr-post :access-token "abc"))]
    (testing "is-accepted? true if a thumb"
      (stubbing [tentacles.issues/issue-comments [{:body "test"}]]
        (is (not (is-accepted? pr)))))
    (testing "is-accepted? false if not thumb"
      (stubbing [tentacles.issues/issue-comments [{:body ":+1:"}]]
        (is (is-accepted? pr))))
    (testing "set-appropriate-state"
      ; I don't like how far back into the implementation I'm reaching for these
      ; stubs, but I was having trouble making valuable shrubbery tests.
      (stubbing [tentacles.core/api-call true
                 tentacles.issues/issue-comments [{:body ":+1:"}]]
        (set-appropriate-state pr)
        (verify-call-times-for tentacles.core/api-call 1)))))

(deftest github-function-tests
  (let [pr (pull-request-event->PullRequest (assoc test-pr-post :access-token "abc"))]
    (testing "it fetches comments"
      (mocking [tentacles.issues/issue-comments]
        (get-comments pr)
        (verify-call-times-for tentacles.issues/issue-comments 1)))
    (testing "setting state"
      (mocking [tentacles.core/api-call]
        (testing "with a pending state"
          (set-state pr :pending)
          (verify-call-times-for tentacles.core/api-call 1))))))
