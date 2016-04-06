(ns cljs.user
  (:require [figwheel.client :as figwheel]
            [dertwothumber.frontend.core :as core]))

(js/console.info "Starting in development mode")

(enable-console-print!)

(figwheel/start {:websocket-url "ws://192.168.99.100:3449/figwheel-ws"})

(core/init!)
