(ns dertwothumber.frontend.component.mounter
  (:require [com.stuartsierra.component :as component]
            [reagent.core :as r]))

(defn- middleware-fn [mounter m]
  (if (vector? m)
    (let [[f & keys] m
          args       (map (fn [k] (k mounter)) keys)]
      (apply f args))
    m))

(defn- middleware-wrapper [mounter]
  (apply comp (map (partial middleware-fn mounter)
                   (:middleware mounter [(fn [view] view)]))))

(defrecord Mounter []
  component/Lifecycle
  (start [{:keys [mount-point router] :as mounter}]
    (let [current-view    (:current-view router)
          wrap-middleware (middleware-wrapper mounter)]

      (r/render-component [wrap-middleware @current-view]
                          (.getElementById js/document mount-point))))

  (stop [{:keys [mount-point] :as mounter}]
    (r/unmount-component-at-node mount-point)))

(defn mounter [config]
  (map->Mounter config))
