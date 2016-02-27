(ns dertwothumber.endpoint.ui
  (:require [compojure.core :refer :all]
            [dertwothumber.static-view.loading-page :as loading-page])
  (:use [ring.middleware.session]
        [ring.util.response]))

(defn ui-endpoint [config]
  (context "/ui" []
    (GET "*" {session :session}
      (-> (response (loading-page/loading-page :initial-state session))
          (content-type "text/html ")))))
