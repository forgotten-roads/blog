(ns mx.roads.forgotten.blog.cli.show
  (:require [clojure.pprint :refer [pprint]]
            [clojusc.twig :as logger]
            [mx.roads.forgotten.blog.config :as config]
            [mx.roads.forgotten.blog.meta :as meta]
            [mx.roads.forgotten.blog.util :as util]
            [taoensso.timbre :as log])
  (:refer-clojure :exclude [meta]))

(defn help-cmd
  [& args]
  (util/print-docstring 'mx.roads.forgotten.blog.cli.show 'run))

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
    :all (pprint (config/blog))
    :port (pprint (config/get-port))
    :metadata (if-let [post (first args)]
                (pprint (meta/get post))
                (pprint (meta/get-all)))
    :help (help-cmd args)
    (pprint (config/blog))))
