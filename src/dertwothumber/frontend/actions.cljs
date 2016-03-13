(ns dertwothumber.frontend.actions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [dertwothumber.frontend.api.repos :as repos]
            [reagent.session :as session]
            [dertwothumber.frontend.page.repos :as repo-page]
            [cljs.core.async :refer [<!]]))

(defn repo-request []
  (go
    (let [user-repos (<! (repos/fetch-repos))]
      (session/put! :repos user-repos)
      (println "putting " #'repo-page/repo-page)
      (session/put! :current-page #'repo-page/repo-page))))
