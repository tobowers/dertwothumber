(ns dertwothumber.endpoint.endpoint-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [kerodon.core :refer :all]
            [kerodon.test :refer :all]
            [dertwothumber.endpoint.ui :as ui]))

(def handler
  (ui/ui-endpoint {}))

(deftest smoke-test
  (testing "it handles arbitrary urls"
    (-> (session handler)
        (visit "/ui/junk-url-that-we-know-doesnot-exist")
        (has (status? 200) "page exists"))))
