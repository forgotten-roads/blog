(ns mx.roads.forgotten.blog.cli
  (:require [dragon.config :as config]
            [mx.roads.forgotten.blog.cli.new :as new]
            [mx.roads.forgotten.blog.cli.show :as show]
            [mx.roads.forgotten.blog.core :as core]
            [taoensso.timbre :as log]
            [trifl.docs :as docs]))

(defn help-cmd
  [& args]
  (docs/print-docstring 'mx.roads.forgotten.blog.cli 'run))

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
  (core/set-log-level)
  (log/debug "CLI got cmd:" cmd)
  (log/debug "CLI got args:" args)
  (case cmd
    :new (new/run args)
    :show (show/run args)
    :gen (core/generate)
    :run (core/generate+web)
    :help (help-cmd args)
    :version (print (core/version))
    ;; Aliases
    :--help (help-cmd args)
    :--version (print (core/version))
    :-h (help-cmd args)
    :-v (print (core/version)))
  (shutdown-agents))
