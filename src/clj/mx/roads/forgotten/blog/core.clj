(ns mx.roads.forgotten.blog.core
  (:require [clojusc.twig :as logger]
            [dragon.config :as config]
            [dragon.generator :as gen]
            [dragon.util :as util]
            [dragon.web :as web]
            [mx.roads.forgotten.blog.routes :refer [gen-routes routes]]
            [trifl.core :refer [sys-prop]]
            [trifl.docs :as docs]))

(defn set-log-level
  []
  (logger/set-level! (config/log-ns) (config/log-level)))

(defn version
  []
  (let [version (sys-prop "blog.version")
        build (util/get-build)]
    (format "FRMX Blog version %s, build %s\n" version build)))

(defn generate
  []
  (gen/run (gen-routes (config/posts-path)) (config/output-dir)))

(defn web
  []
  (web/run
    (routes (config/posts-path))
            (config/port)
            (config/output-dir)))

(defn generate+web
  []
  (generate)
  (web))

(defn log+generate+web
  []
  (set-log-level)
  (generate+web))
