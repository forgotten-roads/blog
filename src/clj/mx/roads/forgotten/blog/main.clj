(ns mx.roads.forgotten.blog.main
  (:require [dragon.config :as config]
            [mx.roads.forgotten.blog.cli :as cli]
            [mx.roads.forgotten.blog.core :as core]
            [taoensso.timbre :as log])
  (:gen-class))

(defn -main
  "This is the entry point for the FRMX Blog Application.

  It manages both the running of the application and related services, as well
  as use of the application name spaces for running tasks on the comand line.

  The entry point is executed from the command line when calling `lein run`."
  ([]
    (-main :web))
  ([mode & args]
    ;; Set the initial log-level before the components set the log-levels for
    ;; the configured namespaces
    (core/set-log-level)
    (log/infof "Running FRMX Blog application in %s mode ..." mode)
    (log/debug "Passing the following args to the application:" args)
    (case (keyword mode)
      :web (core/web)
      :cli (cli/run (map keyword args)))))
