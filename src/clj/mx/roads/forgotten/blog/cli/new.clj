(ns mx.roads.forgotten.blog.cli.new
  (:require [clojure.pprint :refer [pprint]]
            [clojusc.twig :as logger]
            [mx.roads.forgotten.blog.cli.new.post :as post]
            [mx.roads.forgotten.blog.util :as util]
            [taoensso.timbre :as log]))

(defn help-cmd
  [& args]
  (util/print-docstring 'mx.roads.forgotten.blog.cli.new 'run))

(defn run
  "
  Usage:
  ```
    frmx new [SUBCOMMAND | help]
  ```

  If no SUBCOMMAND is provided, the default 'post' will be used (with the
  default content type ':md').

  Subcommands:
  ```
    post    Create a new post stub; takes a subcommand for the type of
              content to create; see 'frmx new post help' for usage
  ```"
  [[cmd & args]]
  (log/debug "Got cmd:" cmd)
  (log/debug "Got args:" args)
  (case cmd
    :post (post/run args)
    :help (help-cmd)
    (post/run [:md])))


