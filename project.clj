(defn get-banner
  []
  ; (str
  ;   (slurp "resources/text/repl-banner.txt")
  ;   (slurp "resources/text/repl-loading.txt")))
  "")

(defn get-prompt
  [ns]
  (str "\u001B[35m[\u001B[34m"
       ns
       "\u001B[35m]\u001B[33m λ\u001B[m=> "))

(defproject mx.roads.forgotten/blog "0.3.0-SNAPSHOT"
  :description "Blog for Forgotten Roads MX"
  :url "https://forgotten.roads.mx/blog/"
  :scm {
    :name "git"
    :url "https://github.com/forgotten-roads/blog"}
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :exclusions [
    [org.clojure/clojure]
    [org.clojure/clojurescript]]
  :dependencies [
    [clojusc/env-ini "0.3.0"]
    [clojusc/rfc5322 "0.3.0"]
    [clojusc/trifl "0.2.0-SNAPSHOT"]
    [clojusc/twig "0.3.2-SNAPSHOT"]
    [com.draines/postal "2.0.2"]
    [dragon "0.3.0-SNAPSHOT"]
    [markdown-clj "0.9.99"]
    [me.raynes/cegdown "0.1.1"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/data.xml "0.0.8"]
    [org.clojure/math.combinatorics "0.1.4"]
    [ring/ring-core "1.6.2"]
    [selmer "1.11.0"]
    [stasis "2.3.0"]
    [tentacles "0.5.1"]
    ;; XXX remove these:
    [org.clojure/java.classpath "0.2.3"]]
  :source-paths ["src/clj"]
  :dragon {
    :domain "forgotten.roads.mx/blog"
    :name "Forgotten Roads MX"
    :description ~(str "Articles, Reviews, & Explorations for the Motorcycle "
                       "Excursionist & Non-traditional Adventurer")
    :dev-port 5096
    :output-dir "."
    :base-path "/blog"
    :posts-path "/blog/archives"
    :posts-path-src "./posts"
    :feed-count 20
    :cli {
      :log-level :info
      :log-ns [mx.roads dragon]}}
  :profiles {
    :uberjar {:aot :all}
    :custom-repl {
      :repl-options {
        :init-ns mx.roads.forgotten.blog.dev
        :prompt ~get-prompt
        ;:init ~(println (get-banner))
        }}
    :dev {
      :source-paths ["dev-resources/src"]
      :main mx.roads.forgotten.blog.main
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
        [venantius/yagni "0.1.4"]]}
    :cli {
      :resource-paths ["posts"]}}
  :aliases {
    "repl"
      ^{:doc (str "A custom FRMX REPL that overrides the default one")}
      ["with-profile" "+custom-repl" "repl"]
    "check-deps"
      ^{:doc (str "Check if any deps have out-of-date versions")}
      ["with-profile" "+test" "ancient" "check" "all"]
    "lint"
      ^{:doc (str "Perform lint checking")}
      ["with-profile" "+test" "kibit"]
    "frmx"
      ^{:doc (str "The FRMX Blog CLI; type `lein frmx help` for commands")}
      ["with-profile" "+cli"
       "run" "-m" "mx.roads.forgotten.blog.main" "cli"]
    "gen"
      ^{:doc (str "Generate static content for the blog")}
      ["run" "-m" "mx.roads.forgotten.blog.core/generate"]
    "web"
      ^{:doc (str "Run a local web service for the blog")}
      ["run" "-m" "mx.roads.forgotten.blog.core/web"]
    "dev"
      ^{:doc (str "Generate blog content and run local web service")}
      ["run" "-m" "mx.roads.forgotten.blog.core/log+generate+web"]
    "build"
      ^{:doc (str "Perform build tasks for CI/CD & releases\n\n"
                 "Additional aliases:")}
      ["with-profile" "+test" "do"
        ["check-deps"]
        ["lint"]
        ["test"]
        ["compile"]
        ["uberjar"]]})
