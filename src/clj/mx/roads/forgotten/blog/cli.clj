(ns mx.roads.forgotten.blog.cli
  (:require [clojure.pprint :refer [pprint]]
            [clojusc.twig :as logger]
            [dragon.config :as config]
            [dragon.generator :as gen]
            [dragon.util :as util]
            [dragon.web :as web]
            [mx.roads.forgotten.blog.cli.new :as new]
            [mx.roads.forgotten.blog.cli.show :as show]
            [mx.roads.forgotten.blog.routes :refer [gen-routes routes]]
            [taoensso.timbre :as log]
            [trifl.core :refer [sys-prop]]
            [trifl.docs :as docs]))

(defn help-cmd
  [& args]
  (docs/print-docstring 'mx.roads.forgotten.blog.cli 'run))

(defn version-cmd
  []
  (let [version (System/getProperty "blog.version")
        build (util/get-build)]
    (print (format "FRMX Blog version %s, build %s\n" version build))))

(defn generate
  []
  (gen/run (gen-routes (config/posts-path)) (config/output-dir)))

(defn web
  []
  (web/run (routes (config/posts-path))
                  (config/port)
                  (config/output-dir)))

(defn run
  "
  Usage:
  ```
    frmx COMMAND [help | arg...]
    frmx [-h | --help | -v | --version]
  ```

  Commands:
  ```
    new      Create stubbed files for a new blog post
    show     Display various frmx data in the terminal
    gen      Generate updated static content for blog
    run      Run the FRMX Blog locally as a Ring app
    help     Display this usage message
    version  Display the current NOWA version
  ```

  More information:

    Each command takes an optional 'help' subcommand that will provide
    usage information about the particular command in question, e.g.:

  ```
    $ frmx new help
  ```"
  [[cmd & args]]
  (log/debug "CLI got cmd:" cmd)
  (log/debug "CLI got args:" args)
  (case cmd
    :new (new/run args)
    :show (show/run args)
    :gen (generate)
    :run (web)
    :help (help-cmd args)
    :version (version-cmd)
    ;; Aliases
    :--help (help-cmd args)
    :--version (version-cmd)
    :-h (help-cmd args)
    :-v (version-cmd))
  (shutdown-agents))
