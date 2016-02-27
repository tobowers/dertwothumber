(ns dertwothumber.frontend.page.home
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]))

;; -------------------------
;; Views

(defn home-page []
  [:div
    [:h1 "Welcome to dertwothumber"]
    (if (session/get :access-token)
      [:p "It appears you are logged in"]
      [:div
        [:p "It appears you are not logged in"]
        [:a {:href "/oauth/github"} "Login"]])])
