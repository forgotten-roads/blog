(defproject mx.roads.forgotten/blog "0.2.0-SNAPSHOT"
  :description "Blog for Forgotten Roads MX"
  :url "https://forgotten.roads.mx/blog/"
  :scm {
    :name "git"
    :url "https://github.com/forgotten-roads/blog"}
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :exclusions [
    [org.clojure/clojurescript]]
  :dependencies [
    [clojusc/env-ini "0.3.0"]
    [clojusc/rfc5322 "0.3.0"]
    [clojusc/trifl "0.1.0"]
    [clojusc/twig "0.3.2-SNAPSHOT"]
    [dragon "0.2.0-SNAPSHOT"]
    [markdown-clj "0.9.99"]
    [me.raynes/cegdown "0.1.1"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/data.xml "0.0.8"]
    [ring/ring-core "1.6.1"]
    [selmer "1.10.7"]
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
    :output-dir "."
    :posts-path "/blog/archives"
    :feed-count 20
    :cli {
      :log-level :debug
      :log-ns [mx.roads dragon stasis]}}
  :profiles {
    :uberjar {:aot :all}
    :dev {
      :source-paths ["dev-resources/src"]
      :main mx.roads.forgotten.blog.main
      :aliases {"frmx" ^{:doc (str "The FRMX Blog CLI; "
                                   "type `lein frmx help` for commands\n")}
                       ["run" "-m" "mx.roads.forgotten.blog.main" "cli"]}
      :repl-options {
        :init-ns mx.roads.forgotten.blog.dev
        :prompt (fn [ns] (str "\u001B[35m[\u001B[34m"
                              ns
                              "\u001B[35m]\u001B[33m λ\u001B[m=> "))}
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
        [venantius/yagni "0.1.4"]]}}
  :aliases {
    "check-deps" ["with-profile" "+test" "ancient" "check" "all"]
    "lint" ["with-profile" "+test" "kibit"]
    "build" ["with-profile" "+test" "do"
      ["check-deps"]
      ["lint"]
      ["test"]
      ["compile"]
      ["uberjar"]]})
