(ns mx.roads.forgotten.blog.main
  (:require [dragon.components.system :as components]
            [dragon.config :as config]
            [mx.roads.forgotten.blog.cli.core :as cli]
            [mx.roads.forgotten.blog.core :as core]
            [taoensso.timbre :as log]
            [trifl.java :as trifl])
  (:gen-class))

(defn -main
  "This is the entry point for the FRMX Blog Application.

  It manages both the running of the application and related services, as well
  as use of the application name spaces for running tasks on the comand line.

  The entry point is executed from the command line when calling `lein run`."
  ([]
    (-main :web))
  ([mode & args]
   (let [system (components/start)]
     (log/infof "Running FRMX Blog application in %s mode ..." mode)
     (log/debug "Passing the following args to the application:" args)
     (case (keyword mode)
       :web (core/generate+web system)
       :cli (cli/run system (map keyword args)))
     ;; Do a full shut-down upon ^c
     (trifl/add-shutdown-handler #(components/stop system)))))
