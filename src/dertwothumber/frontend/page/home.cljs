(ns dertwothumber.frontend.page.home
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]))

;; -------------------------
;; Views

(defn home-page []
  [:div
    [:h1 "Welcome to dertwothumber"]
    (if (session/get :access-token)
      [:div
       [:p "It appears you are logged in"]
       [:p (str "user: " (session/get :user))]
      ]
      [:div
        [:p "It appears you are not logged in"]
        [:a {:href "/oauth/github"} "Login"]])])
