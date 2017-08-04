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
  (let [generated-routes (gen-routes (config/base-path)
                                     (config/posts-path))]
    (gen/run generated-routes
             (config/output-dir))))

(defn web
  [generated-routes]
  (web/run
    generated-routes
    (config/port)
    (config/output-dir)))

(defn generate+web
  []
  (web (generate)))

(defn log+generate
  []
  (set-log-level)
  (generate))

(defn log+generate+web
  []
  (set-log-level)
  (generate+web))
