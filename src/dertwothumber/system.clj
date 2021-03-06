(ns dertwothumber.system
  (:require [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [duct.component.endpoint :refer [endpoint-component]]
            [duct.component.handler :refer [handler-component]]
            [duct.middleware.not-found :refer [wrap-not-found]]
            [duct.middleware.route-aliases :refer [wrap-route-aliases]]
            [meta-merge.core :refer [meta-merge]]
            [ring.component.jetty :refer [jetty-server]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.webjars :refer [wrap-webjars]]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [dertwothumber.endpoint.oauth :refer [oauth-endpoint]]
            [dertwothumber.endpoint.webhooks :refer [webhooks-endpoint]]
            [dertwothumber.endpoint.api.repos :refer [repos-endpoint]]
            [dertwothumber.endpoint.ui :refer [ui-endpoint]]
            [dertwothumber.component.dynamo-db :refer [dynamo-db-component]]
            [dertwothumber.component.user :refer [user-component]]
            [dertwothumber.component.repo :refer [repo-component]])
  (:use [ring.middleware.session.cookie]))

(defn- deep-merge [a b]
  (merge-with (fn [x y]
                (cond (map? y) (deep-merge x y)
                      (vector? y) (concat x y)
                      :else y))
              a b))

(def base-config
  {:app {:middleware [[wrap-not-found :not-found]
                      [wrap-webjars]
                      [wrap-defaults :defaults]
                      [wrap-route-aliases :aliases]
                      [wrap-authentication :authentication]]
         :not-found  (io/resource "dertwothumber/errors/404.html")
         :defaults   (meta-merge site-defaults {:static  {:resources "dertwothumber/public"}
                                                :session {:store (cookie-store {:key "a 16-byte sekret"})
                                                          :cookie-attrs {:max-age 3600}}
                                                :security  {:anti-forgery true}})
         :aliases    {"/" "/ui"}}})

(def api-config
  (deep-merge base-config {:app {:defaults {:security {:anti-forgery false}}}}))

(defn new-system [config]
  (let [config (meta-merge base-config config)]
    (-> (component/system-map
          :app  (handler-component (:app config))
          :api  (handler-component (:app api-config))
          :http (jetty-server (:http config))
          :oauth (endpoint-component (oauth-endpoint (:github config)))
          :ui (endpoint-component ui-endpoint)
          :repos-endpoint (endpoint-component repos-endpoint)
          :webhooks (endpoint-component webhooks-endpoint)
          :dynamo-db (dynamo-db-component (:dynamodb config))
          :user-db (user-component)
          :repo (repo-component (:github config)))
        (component/system-using
         {:http [:app]
          :app  [:oauth :ui :repos-endpoint]
          :api  [:webhooks]
          :ui []
          :webhooks [:repo]
          :repos-endpoint [:repo]
          :oauth []
          :dynamo-db []
          :user-db [:dynamo-db]
          :repo [:dynamo-db]}))))
