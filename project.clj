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

(defproject mx.roads.forgotten/blog "0.4.0-SNAPSHOT"
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
    [clojusc/rfc5322 "0.4.0"]
    [clojusc/trifl "0.2.0"]
    [clojusc/twig "0.3.2"]
    ;; XXX Remove the following once the next dragon snapshot is pushed to Clojars
    [com.datomic/clj-client "0.8.606"]
    [com.taoensso/carmine "2.16.0"]
    ;; XXX END
    [com.stuartsierra/component "0.3.2"]
    [dragon "0.4.0-SNAPSHOT"]
    ;; dragon deps
    [com.datomic/clj-client "0.8.606"]
    [com.taoensso/carmine "2.16.0" :exclusions [
      com.taoensso/encore
      com.taoensso/truss]]
    [pandect "0.6.1"]
    ;; dragon deps end
    [markdown-clj "1.0.1"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/data.generators "0.1.2"]
    [org.clojure/data.xml "0.0.8"]
    [org.clojure/math.combinatorics "0.1.4"]
    [ring/ring-core "1.6.2"]
    [selmer "1.11.1"]
    [stasis "2.3.0"]]
  :source-paths ["src/clj"]
  :dragon {
    :domain "forgotten.roads.mx/blog"
    :name "Forgotten Roads MX"
    :description ~(str "Articles, Reviews, & Explorations for the Motorcycle "
                       "Excursionist & Non-traditional Adventurer")
    :port 5096
    :output-dir "."
    :base-path "/blog"
    :posts-path "/blog/archives"
    :posts-path-src "./posts"
    :feed-count 20
    :cli {
      :log-level :info
      :log-nss [mx.roads]}
    :workflow {
     :storage :memory}}
  :profiles {
    :ubercompile {:aot :all}
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
        [lein-ancient "0.6.12"]
        [jonase/eastwood "0.2.4"]
        [lein-bikeshed "0.4.1" :exclusions [org.clojure/tools.namespace]]
        [lein-kibit "0.1.5"]
        [venantius/yagni "0.1.4"]]}
    :cli {
      :resource-paths ["posts"]
      :exclusions [
        clj-http
        clojusc/cljs-tools
        common-codec
        commons-logging
        joda-time
        org.apache.maven.wagon/wagon-http
        org.clojure/clojurescript
        org.clojure/clojure]
      :dependencies [
        [clj-http "2.0.1"]
        [clojusc/cljs-tools "0.2.0-SNAPSHOT"]
        [com.draines/postal "2.0.2"]
        [com.google.api-client/google-api-client "1.22.0"]
        [com.google.apis/google-api-services-plusDomains "v1-rev434-1.22.0"]
        [commons-codec "1.10"]
        [commons-logging "1.2"]
        [joda-time "2.9.9"]
        [org.apache.maven.wagon/wagon-http "2.10"]
        [org.clojure/data.json "0.2.6"]
        [twitter-api "1.8.0"]]}}
  :aliases {
    "repl"
      ^{:doc (str "A custom FRMX REPL that overrides the default one")}
      ["with-profile" "+test,+custom-repl,+cli" "repl"]
    "check-deps"
      ^{:doc (str "Check if any deps have out-of-date versions")}
      ["with-profile" "+test" "ancient" "check" ":all"]
    "lint"
      ^{:doc (str "Perform lint checking")}
      ["with-profile" "+test" "kibit"]
    "frmx"
      ^{:doc (str "The FRMX Blog CLI; type `lein frmx help` or `frmx help` "
                  "for commands")}
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
      ["with-profile" "+test,+cli" "do"
        ;["check-deps"]
        ;["lint"]
        ["test"]
        ["compile"]
        ["uberjar"]]})
