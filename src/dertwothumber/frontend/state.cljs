(ns dertwothumber.frontend.state
  (:refer-clojure :exclude [get get-in reset! swap!])
  (:require [reagent.core :as reagent :refer [atom]]
            [com.stuartsierra.component :as component]))

(defprotocol State
  (get [this key] "get a key from storage")
  (put [this key val] "set a key on the storage")
  (reset [this state] "reset to the state "))

(defrecord State []
  component/Lifecycle
  (start [this]
    (println "setting up state")
    (assoc this :atom (atom {})))
  (stop [this]
    (dissoc this :atom))
  State
  (get [this key]
    (clojure.core/get @(:atom this) key))
  (put [this key val]
    (clojure.core/swap! (:atom this) assoc key val))
  (reset [this replacement-state]
    (clojure.core/reset! (:atom this) state)))

(defn new-state []
  (State.))
