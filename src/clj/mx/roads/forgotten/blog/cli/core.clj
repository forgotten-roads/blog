(ns mx.roads.forgotten.blog.cli.core
  (:require [dragon.config.core :as config]
            [dragon.event.system.core :as event]
            [dragon.event.tag :as tag]
            [mx.roads.forgotten.blog.cli.new :as new]
            [mx.roads.forgotten.blog.cli.show :as show]
            [mx.roads.forgotten.blog.cli.share :as share]
            [mx.roads.forgotten.blog.core :as core]
            [taoensso.timbre :as log]
            [trifl.docs :as docs]))

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
    share    Post blog updates (saved in files) to various services
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
  [system [cmd & args]]
  (log/debug "CLI got cmd:" cmd)
  (log/debug "CLI got args:" args)
  (event/publish system tag/run-cli {:cmd cmd :args args})
  (case cmd
    :new (new/run system args)
    :show (show/run system args)
    :gen (core/generate system)
    :share (share/run system args)
    :run (core/generate system)
    :help (docs/print-docstring #'run)
    :version (print (core/version))
    ;; Aliases
    :--help (docs/print-docstring #'run)
    :--version (print (core/version))
    :-h (docs/print-docstring #'run)
    :-v (print (core/version)))
  (event/publish system tag/shutdown-cli))
