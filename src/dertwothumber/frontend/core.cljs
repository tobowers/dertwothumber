(ns dertwothumber.frontend.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [dertwothumber.frontend.page.home :as home-page]))

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
                    (session/put! :current-page #'home-page/home-page))

(secretary/defroute "/ui" []
                    (session/put! :current-page #'home-page/home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (session/reset! (js->clj (.-__initialState js/window)))
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
