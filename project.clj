(defproject nl.goodger/hsluv-clj "4.0-alpha1"
  :description "A Clojure implementation of HSLuv."
  :url "https://github.com/goodgerster/hsluv-clj"
  :license {:name         "MIT"
            :url          "https://opensource.org/licenses/MIT"
            :distribution :repo}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :managed-dependencies []
  :pedantic? :abort
  :aot [hsluv-clj.core]
  :repl-options {:init-ns hsluv-clj.core}
  :profiles {:dev {:dependencies [[junit "3.8.2"]
                                  [org.glassfish/javax.json "1.0.4"]]}})
