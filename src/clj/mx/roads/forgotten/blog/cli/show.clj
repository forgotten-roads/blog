(ns mx.roads.forgotten.blog.cli.show
  (:require [clojure.pprint :refer [pprint]]
            [clojusc.twig :as logger]
            [dragon.config :as config]
            [dragon.util :as util]
            [taoensso.timbre :as log]
            [trifl.docs :as docs])
  (:refer-clojure :exclude [meta]))

(defn run
  "
  Usage:
  ```
    frmx show [SUBCOMMAND | help]
  ```

  If no SUBCOMMAND is provided, the default 'config' will be used.

  Subcommands:
  ```
    config          Display the current blog configuration
    port            Display the HTTP port configuration
    metadata        Display the metadata for all posts
    metadata POST   Display the metadata for a given blog post
  ```"
  [system [cmd & args]]
  (log/debug "Got cmd:" cmd)
  (log/debug "Got args:" args)
  (case cmd
    :all (pprint (:config system))
    :port (pprint  (config/port system))
    :metadata ;(if-let [post (first args)]
              (println "\nCurrently this operation is not supported.\n")
    :help (docs/print-docstring #'run)
    (pprint (:config system))))
