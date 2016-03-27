(ns dertwothumber.frontend.component.view
  (:require [com.stuartsierra.component :as component]))

(defrecord View [build-route]
  component/Lifecycle
  (start [view]
    (if-not (:route view)
      (assoc view :route (build-route view))
      view))

  (stop [view]
    (dissoc view :route)))

(defn view [build-route]
  (->View build-route))
