(ns dertwothumber.frontend.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [dertwothumber.frontend.page.home :as home-page]
            [dertwothumber.frontend.page.loading :as loading-page]
            [dertwothumber.frontend.page.repos :as repo-page]
            [dertwothumber.frontend.api.repos :as repos]
            [cljs.core.async :refer [<!]]))

(defn current-page []
  [:div [(session/get :current-page)]])

(defn repo-request []
  (go
    (let [user-repos (<! (repos/fetch-repos))]
      (session/put! :repos user-repos)
      (session/put! :current-page #'repo-page/repo-page))))

;; -------------------------
;; Routes

(secretary/defroute "/" []
                    (session/put! :current-page #'home-page/home-page))

(secretary/defroute "/ui" []
                    (session/put! :current-page #'home-page/home-page))

(secretary/defroute "/ui/repos" []
                    (repo-request))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (session/reset! (js->clj (.-__initialState js/window) :keywordize-keys true))
  (accountant/configure-navigation!)
  (if-not (session/get :current-page)
    (session/put! :current-page #'loading-page/loading-page))
  (accountant/dispatch-current!)
  (mount-root))
