(ns dertwothumber.frontend.page.home
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to dertwothumber"]])
