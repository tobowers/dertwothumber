(ns dertwothumber.frontend.actions
  (:require [dertwothumber.frontend.api.repos :as repos]
            [reagent.session :as session]
            [dertwothumber.frontend.page.repos :as repo-page]))

(defn repo-request []
  (let [user-repos (repos/fetch-repos)]
    (session/put! :repos user-repos)
    (session/put! :current-page #'repo-page/repo-page)
  ))
