(ns dertwothumber.static-views.loading-page
  (:require [cheshire.core :refer :all])
  (:use [hiccup.core]
        [hiccup.page]
        ))

(defn loading-page
  [& {:keys [content initial-state]}]
  (html5 [:head {:lang "en"}
                [:title "Welcome to duct"]
                (include-css "/assets/normalize.css/normalize.css")
                (include-css "/css/site.css")]
         [:body
           (if content [:div content])
           [:div {:id "app"} "Loading..."]
           [:script {:type "text/javascript"} (generate-string {:__initialState initial-state})]
           (include-js "/js/main.js")]))
