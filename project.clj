(defproject dertwothumber "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [com.stuartsierra/component "0.3.0"]
                 [compojure "1.4.0"]
                 [duct "0.5.8"]
                 [environ "1.0.2"]
                 [meta-merge "0.1.1"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring-jetty-component "0.3.0"]
                 [ring-webjars "0.1.1"]
                 [org.slf4j/slf4j-nop "1.7.14"]
                 [org.webjars/normalize.css "3.0.2"],
                 [reagent "0.5.1"
                  :exclusions [org.clojure/tools.reader]]
                 [reagent-forms "0.5.16"]
                 [reagent-utils "0.1.7"]
                 [hiccup "1.0.5"]
                 [secretary "1.2.3"]
                 [venantius/accountant "0.1.6"
                  :exclusions [org.clojure/tools.reader]]
                 [tentacles "0.5.1"]
                 [cljs-http "0.1.39"]
                 [clj-http "2.1.0"]
                 [ring/ring-mock "0.3.0"]
                 [clj-http-fake "1.0.2"]
                 [buddy/buddy-auth "0.9.0"]
                 [cheshire "5.5.0"]
                 [com.taoensso/faraday "1.8.0"]
                 [com.cognitect/transit-cljs "0.8.237"]]
  :plugins [[lein-environ "1.0.2"]
            [lein-gen "0.2.2"]
            [lein-cljsbuild "1.1.2"]
            [lein-doo "0.1.6"]]
  :generators [[duct/generators "0.5.8"]]
  :duct {:ns-prefix dertwothumber}
  :main ^:skip-aot dertwothumber.main
  :target-path "target/%s/"
  :resource-paths ["resources" "target/cljsbuild"]
  :prep-tasks [["javac"] ["cljsbuild" "once"] ["compile"]]
  :cljsbuild
  {:builds
   {:main {:jar true
           :source-paths ["src"]
           :compiler {:output-to "target/cljsbuild/dertwothumber/public/js/main.js"
                      :optimizations :advanced}}
    :test {:source-paths ["src" "test"]
           :compiler {:output-to "resources/public/js/testable.js"
                      :main dertwothumber.test-runner
                      :optimizations :none
                      :target :nodejs}}}}

  :aliases {"gen"   ["generate"]
            "setup" ["do" ["generate" "locals"]]}
  :profiles
  {:dev  [:project/dev  :profiles/dev]
   :test [:project/test :profiles/test]
   :repl {:resource-paths ^:replace ["resources" "target/figwheel"]
          :prep-tasks     ^:replace [["javac"] ["compile"]]}
   :uberjar {:aot :all}
   :profiles/dev  {}
   :profiles/test {}
   :project/dev   {:dependencies [[reloaded.repl "0.2.1"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [eftest "0.1.0"]
                                  [kerodon "0.7.0"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [duct/figwheel-component "0.3.1"]
                                  [figwheel "0.5.0-1"]]
                   :source-paths ["dev"]
                   :repl-options {:init-ns user
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :env {:port 3000}}
   :project/test  {:env {:github-client-id "fake-github-client-id"
                         :github-client-secret "fake-github-client-secret"
                         :app-host "http://fake-host:3000"}}})
