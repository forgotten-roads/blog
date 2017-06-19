(ns mx.roads.forgotten.blog.cli.show
  (:require [clojure.pprint :refer [pprint]]
            [clojusc.twig :as logger]
            [dragon.config :as config]
            [dragon.meta :as meta]
            [dragon.util :as util]
            [taoensso.timbre :as log]
            [trifl.docs :as docs])
  (:refer-clojure :exclude [meta]))

(defn help-cmd
  [& args]
  (docs/print-docstring 'mx.roads.forgotten.blog.cli.show 'run))

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
  [[cmd & args]]
  (log/debug "Got cmd:" cmd)
  (log/debug "Got args:" args)
  (case cmd
    :all (pprint (config/dragon))
    :port (pprint (config/port))
    :metadata (if-let [post (first args)]
                (pprint (meta/get post))
                (pprint (meta/get-all)))
    :help (help-cmd args)
    (pprint (config/dragon))))
