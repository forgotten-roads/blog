(ns mx.roads.forgotten.blog.config
  (:require [leiningen.core.project :as project]
            [mx.roads.forgotten.blog.util :as util]
            [taoensso.timbre :as log]))

(defn all
  []
  (project/read))

(defn blog
  []
  (:blog (all)))

(defn get-port
  []
  (:dev-port (blog)))

(defn get-output-dir
  []
  (:output-dir (blog)))

(defn cli
  []
  (:cli (blog)))

(defn log-level
  []
  (:log-level (cli)))
