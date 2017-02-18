(defproject mx.roads.forgotten/blog "0.1.0-SNAPSHOT"
  :description "Blog for Forgotten Roads MX"
  :url "https://blog.forgotten.roads.mx/"
  :scm {
    :name "git"
    :url "https://github.com/forgotten-roads/forgotten-roads.github.io"
  }
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
    [clojusc/env-ini "0.3.0-SNAPSHOT"]
    [clojusc/twig "0.3.1-SNAPSHOT"]
    [com.cemerick/url "0.1.1"]
    [com.stuartsierra/component "0.3.2"]
    [compojure "1.5.2"]
    [http-kit "2.2.0"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/data.json "0.2.6"]
    [org.webjars/bootstrap "3.3.7-1"]
    [org.webjars/font-awesome "4.7.0"]
    [potemkin "0.4.3"]
    [ring-logger-timbre "0.7.5"]
    [ring-webjars "0.1.1"]
    [ring/ring-anti-forgery "1.1.0-beta1"]
    [ring/ring-core "1.6.0-beta6"]
    [ring/ring-defaults "0.3.0-beta1"]
    [ring/ring-devel "1.6.0-beta6"]
    [ring/ring-jetty-adapter "1.6.0-beta6"]
    [ring/ring-json "0.4.0"]
    [selmer "1.10.5"]
    [stasis "2.3.0"]]
  :profiles {
    :uberjar {:aot :all}
    :dev {
      :source-paths ["dev-resources/src"]
      :plugins [[lein-simpleton "1.3.0"]]
      :repl-options {
        :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]
        :init-ns mx.roads.forgotten.blog.dev}
        :dependencies [
          [org.clojure/tools.nrepl "0.2.12"]]}
    :test {
      :plugins [
        [lein-ancient "0.6.10"]
        [jonase/eastwood "0.2.3" :exclusions [org.clojure/clojure]]
        [lein-bikeshed "0.4.1"]
        [lein-kibit "0.1.2" :exclusions [org.clojure/clojure]]
        [venantius/yagni "0.1.4"]]}})
