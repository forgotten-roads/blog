(defproject mx.roads.forgotten/blog "0.1.0-SNAPSHOT"
  :description "Blog for Forgotten Roads MX"
  :url "https://blog.forgotten.roads.mx/"
  :scm {
    :name "git"
    :url "https://github.com/forgotten-roads/forgotten-roads.github.io"}
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
    [clojusc/env-ini "0.3.0-SNAPSHOT"]
    [clojusc/twig "0.3.1-SNAPSHOT"]
    [me.raynes/cegdown "0.1.1"]
    [org.clojure/clojure "1.8.0"]
    [selmer "1.10.6"]
    [stasis "2.3.0" :exclusions [ring/ring-codec]]]
  :source-paths ["src/clj"]
  :blog {
    :dev-port 5099
    :output-dir "docs"}
  :profiles {
    :uberjar {:aot :all}
    :dev {
      :source-paths ["dev-resources/src"]
      :main mx.roads.forgotten.blog.main
      :aliases {"frmx" ^{:doc (str "The FRMX Blog CLI; "
                                   "type `lein frmx help` for commands\n")}
                       ["run" "-m" "mx.roads.forgotten.blog.main" "cli"]}
      :repl-options {
        :init-ns mx.roads.forgotten.blog.dev}
      :plugins [
        [lein-simpleton "1.3.0"]]
      :dependencies [
        [leiningen-core "2.7.1"]
        [org.clojure/tools.namespace "0.2.11"]
        [javax.servlet/servlet-api "2.5"]
        [ring/ring-core "1.6.0-beta7"]]
      ;:pedantic? :warn
      }
    :test {
      :plugins [
        [lein-ancient "0.6.10"]
        [jonase/eastwood "0.2.3" :exclusions [org.clojure/clojure]]
        [lein-bikeshed "0.4.1" :exclusions [org.clojure/tools.namespace]]
        [lein-kibit "0.1.2" :exclusions [org.clojure/clojure]]
        [venantius/yagni "0.1.4"]]}})
