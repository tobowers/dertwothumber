(ns dertwothumber.frontend.component.router
  (:require [com.stuartsierra.component :as component]
            [reagent.core :as r :refer [atom]]
            [secretary.core :as secretary]
            [goog.events :as events])
  (:require-macros [secretary.core :refer [route]])
  (:import [goog.history Html5History EventType]))

(defn- history []
  (doto (Html5History.)
    (.setPathPrefix (str js/window.location.protocol
                         "//"
                         js/window.location.host))
    (.setUseFragment false)))

(defn- current-route []
  (str js/window.location.pathname
       js/window.location.search))

(defn- routes [router]
  (keep :route (vals router)))

(defn- dispatcher [router]
  (if-let [routes (seq (routes router))]
    (secretary/uri-dispatcher routes)
    (constantly [:div])))

(defrecord Router []
  component/Lifecycle
  (start [router]
    (if-not (:navigation-listener router)
      (let [history              (:history router (history))
            dispatch             (dispatcher router)
            current-view         (atom (dispatch (current-route)))
            navigation-listener  (fn [e] (reset! current-view
                                                (dispatch (.-token e))))]
        (events/listen history EventType.NAVIGATE navigation-listener)
        (assoc router
               :current-view current-view
               :navigation-listener navigation-listener
               :history history))
      router))

  (stop [router]
    (if-let [navigation-listener (:navigation-listener router)]
      (let [history (events/unlisten (:history router)
                                     EventType.NAVIGATE
                                     navigation-listener)]
        (-> router
            (dissoc :navigation-listener :current-view)
            (assoc :history history)))

      router)))

(defn router [config]
  (map->Router config))
