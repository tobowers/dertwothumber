(ns dertwothumber.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [dertwothumber.frontend.page.home-test]))

(doo-tests 'dertwothumber.frontend.page.home-test)
