(ns mx.roads.forgotten.blog.config
  (:require [leiningen.core.project :as project]
            [mx.roads.forgotten.blog.util :as util]
            [taoensso.timbre :as log]))

(defn lookup
  []
  (:blog (project/read)))

(defn get-port
  []
  (:dev-port (lookup)))

(defn get-output-dir
  []
  (:output-dir (lookup)))
