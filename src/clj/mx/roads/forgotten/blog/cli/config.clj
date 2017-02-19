(ns mx.roads.forgotten.blog.cli.config
  (:require [clojure.pprint :refer [pprint]]
            [clojusc.twig :as logger]
            [mx.roads.forgotten.blog.config :as config]
            [mx.roads.forgotten.blog.util :as util]
            [taoensso.timbre :as log]))

(defn help-cmd
  [& args]
  (util/print-docstring 'mx.roads.forgotten.blog.cli.config 'run))

(defn run
  "
  Usage:
  ```
    frmx config [SUBCOMMAND | help]
  ```

  If no SUBCOMMAND is provided, the default 'all' will be used.

  Subcommands:
  ```
    all      Display the current blog configuration
    port     Display the HTTP port configuration
  ```"
  [[cmd & args]]
  (log/debug "Got cmd:" cmd)
  (log/debug "Got args:" args)
  (case cmd
    :all (pprint (config/blog))
    :port (pprint (config/get-port))
    :help (help-cmd args)
    (pprint (config/blog))))
