(ns dertwothumber.frontend.view.home
  (:require [secretary.core :as secretary])
  (:require-macros [secretary.core :refer [route]]))

(defn home-page []
  [:div
   [:h1 "Welcome to dertwothumber"]
   [:div
    [:p "It appears you are not logged in"]
    [:a {:href "/oauth/github"} "Login"]]])

(defn home-view []
  [:div
   [:h1 "Welcome to dertwothumber"]
   [:div
    [:p "It appears you are logged in"]
    [:p (str "user: " (session/get :user))]]])

(defn home-route [config]
  "/" [] (home-view))
