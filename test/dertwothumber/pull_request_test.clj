(ns dertwothumber.pull-request-test
  (:require [clojure.test :refer :all]
            [dertwothumber.pull-request :refer [pull-request get-comments]]
            [conjure.core :refer [mocking verify-call-times-for verify-first-call-args-for]]
            [tentacles.pulls]))

(deftest testing-pull-requests
  (testing "it fetches comments"
    (let [pr (pull-request {:user-id "user" :repo-name "repo" :pr-id "2" :access-token "abc"})]
      (mocking [tentacles.pulls/comments]
        (get-comments pr)
        (verify-call-times-for tentacles.pulls/comments 1)))))
