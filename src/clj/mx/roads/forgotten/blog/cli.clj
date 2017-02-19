(ns mx.roads.forgotten.blog.cli
  (:require [clojure.pprint :refer [pprint]]
            [clojusc.twig :as logger]
            [mx.roads.forgotten.blog.config :as config]
            [mx.roads.forgotten.blog.generator :as generator]
            [mx.roads.forgotten.blog.util :as util]
            [mx.roads.forgotten.blog.web :as web]
            [taoensso.timbre :as log]))

(defn help-cmd
  [& args]
  (util/print-docstring 'mx.roads.forgotten.blog.cli 'run))

(defn version-cmd
  []
  (let [version (System/getProperty "blog.version")
        build (util/get-build)]
    (print (format "FRMX Blog version %s, build %s\n" version build))))

(defn run
  "
  Usage:
  ```
    frmx COMMAND [help | arg...]
    frmx [-h | --help | -v | --version]
  ```

  Commands:
  ```
    config   Display the current blog configuration
    gen      Generate update static content for blog
    run      Run the FRMX Blog locally as a Ring app
    help     Display this usage message
    version  Display the current NOWA version
  ```

  More information:

    Each command takes an optional 'help' subcommand that will provide
    usage information about the particular command in question, e.g.:

  ```
    $ frmx gen help
  ```"
  [[cmd & args]]
  (log/debug "Got cmd:" cmd)
  (log/debug "Got args:" args)
  (case cmd
    :config (pprint (config/blog))
    :gen (generator/run args)
    :run (web/run)
    :help (help-cmd args)
    :version (version-cmd)
    ;; Aliases
    :--help (help-cmd args)
    :--version (version-cmd)
    :-h (help-cmd args)
    :-v (version-cmd))
  (shutdown-agents))
