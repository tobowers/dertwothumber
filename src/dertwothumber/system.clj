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
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.webjars :refer [wrap-webjars]]
            [dertwothumber.endpoint.example :refer [example-endpoint]])
  (:use [ring.middleware.session.cookie]))

(def base-config
  {:app {:middleware [[wrap-not-found :not-found]
                      [wrap-webjars]
                      [wrap-defaults :defaults]
                      [wrap-route-aliases :aliases]
                      [wrap-session :store-options]]
         :not-found  (io/resource "dertwothumber/errors/404.html")
         :defaults   (meta-merge site-defaults {:static {:resources "dertwothumber/public"}})
         :aliases    {"/" "/index.html"}
         :store-options {:store (cookie-store {:key "a 16-byte secret"})}}})

(defn new-system [config]
  (let [config (meta-merge base-config config)]
    (-> (component/system-map
         :app  (handler-component (:app config))
         :http (jetty-server (:http config))
         :example (endpoint-component example-endpoint))
        (component/system-using
         {:http [:app]
          :app  [:example]
          :example []}))))
