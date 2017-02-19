(ns mx.roads.forgotten.blog.main
  (:require [clojusc.twig :as logger]
            [mx.roads.forgotten.blog.web :as web]
            [mx.roads.forgotten.blog.cli :as cli]
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
    (logger/set-level! ['mx.roads.forgotten.blog] :info)
    (log/infof "Running FRMX Blog application in %s mode ..." mode)
    (log/debug "Passing the following args to the application:" args)
    (case (keyword mode)
      :web (web/run)
      :cli (cli/run (map keyword args)))))
