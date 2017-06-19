(defproject mx.roads.forgotten/blog "0.1.0-SNAPSHOT"
  :description "Blog for Forgotten Roads MX"
  :url "https://forgotten.roads.mx/blog/"
  :scm {
    :name "git"
    :url "https://github.com/forgotten-roads/blog"}
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
    [clojusc/env-ini "0.3.0-SNAPSHOT"]
    [clojusc/rfc5322 "0.3.0-SNAPSHOT"]
    [clojusc/trifl "0.1.0-SNAPSHOT"]
    [clojusc/twig "0.3.1-SNAPSHOT"]
    [dragon "0.1.0-SNAPSHOT"]
    [markdown-clj "0.9.97"]
    [me.raynes/cegdown "0.1.1"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/data.xml "0.0.8"]
    [ring/ring-core "1.6.0-RC1"]
    [selmer "1.10.6"]
    [stasis "2.3.0"]
    [tentacles "0.5.1"]
    ;; XXX remove these:
    [org.clojure/java.classpath "0.2.3"]

    ]
  :source-paths ["src/clj"]
  :dragon {
    :domain "forgotten.roads.mx/blog"
    :name "Blog for Forgotten Roads MX"
    :description "Articles, Reviews, & Explorations for the Motorcycle Excursionist"
    :dev-port 5096
    :output-dir "docs"
    :posts-path "/archives"
    :feed-count 20
    :cli {
      :log-level :info
      :log-ns [mx.roads dragon]}}
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
        [http-kit "2.2.0"]
        [leiningen-core "2.7.1"]
        [org.clojure/tools.namespace "0.2.11"]]
      ;:pedantic? :warn
      }
    :test {
      :plugins [
        [lein-ancient "0.6.10"]
        [jonase/eastwood "0.2.3" :exclusions [org.clojure/clojure]]
        [lein-bikeshed "0.4.1" :exclusions [org.clojure/tools.namespace]]
        [lein-kibit "0.1.2" :exclusions [org.clojure/clojure]]
        [venantius/yagni "0.1.4"]]}})
