(ns dertwothumber.frontend.page.repos
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.session :as session]
            [dertwothumber.frontend.api.repos :as repos]
            [cljs.core.async :refer [<!]]))

;; -------------------------
;; Views

(defn repo-setup [repo-name]
  (go
    (println (<! (repos/setup-repo repo-name)))))

(defn repo-page []
  [:div
    [:h1 "Repos"]
    (let [repos (session/get :repos)]
      (for [repo repos]
        ^{:key (repo "full_name")} [:li {:on-click #(repo-setup (repo "full_name"))} (repo "full_name")]))])
