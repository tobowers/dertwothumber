(ns dertwothumber.frontend.page.repos
  (:require [reagent.session :as session]))

;; -------------------------
;; Views

(defn repo-page []
  [:div
    [:h1 "Repos"]
    (let [repos (session/get :repos)]
      (for [repo repos]
        [:li (:description repos)]))])
