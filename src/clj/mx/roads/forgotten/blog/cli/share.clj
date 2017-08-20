(ns mx.roads.forgotten.blog.cli.share
  (:require [clojure.pprint :refer [pprint]]
            [clojusc.twig :as logger]
            [dragon.config :as config]
            [dragon.meta :as meta]
            [dragon.util :as util]
            [mx.roads.forgotten.blog.email.delivery :as email-delivery]
            [taoensso.timbre :as log]
            [trifl.docs :as docs]))

(defn run
  "
  Usage:
  ```
    frmx share [SUBCOMMAND <post content file> | help]
  ```

  If no SUBCOMMAND is provided, the default 'help' will be used.

  Subcommands:
  ```
    subscribers    Notify email subscribers of the given post
    twitter        Tweet the given post on the FRMX Twitter account
    google+        Create a new FRMX G+ post on the given blog post
  ```

  More information:

    Each command takes an optional 'help' subcommand that will provide
    usage information about how to share, e.g.::

  ```
    $ frmx share email help
  ```"
  [[cmd & [post-file-kw & args]]]
  (let [post-file (name post-file-kw)]
    (log/debug "Got cmd:" cmd)
    (log/debug "Got post-file:" post-file)
    (log/debug "Got args:" args)
    (case cmd
      :subscribers (email-delivery/send-new-post-message post-file)
      ; :twitter ()
      ; :google+ ()
      (docs/print-docstring #'run))))
