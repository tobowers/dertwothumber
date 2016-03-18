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
            [dertwothumber.endpoint.example :refer [example-endpoint]]
            [dertwothumber.endpoint.oauth :refer [oauth-endpoint]]
            [dertwothumber.endpoint.api.repos :refer [repos-endpoint]]
            [dertwothumber.endpoint.ui :refer [ui-endpoint]])
  (:use [ring.middleware.session.cookie]))

(def base-config
  {:app {:middleware [[wrap-not-found :not-found]
                      [wrap-webjars]
                      [wrap-defaults :defaults]
                      [wrap-route-aliases :aliases]
                      [wrap-authentication :authentication]]
         :not-found  (io/resource "dertwothumber/errors/404.html")
         :defaults   (meta-merge site-defaults {:static  {:resources "dertwothumber/public"}
                                                :session {:store (cookie-store {:key "a 16-byte sekret"})
                                                          :cookie-attrs {:max-age 3600}}})
         :aliases    {"/" "/ui"}}
         :authentication {}})

; TODO: make a page that is for logged in - maybe lists repos

(defn new-system [config]
  (let [config (meta-merge base-config config)]
    (-> (component/system-map
         :app  (handler-component (:app config))
         :http (jetty-server (:http config))
         :example (endpoint-component example-endpoint)
         :oauth (endpoint-component oauth-endpoint)
         :ui (endpoint-component ui-endpoint)
         :repos (endpoint-component repos-endpoint)
         :github-config (:github config))
        (component/system-using
         {:http [:app]
          :app  [:example :oauth :ui :repos]
          :example []
          :ui []
          :repos [:github-config]
          :oauth [:github-config]}))))
