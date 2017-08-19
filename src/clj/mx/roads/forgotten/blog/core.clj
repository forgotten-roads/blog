(ns mx.roads.forgotten.blog.core
  (:require [clojusc.twig :as logger]
            [dragon.blog :as blog]
            [dragon.config :as config]
            [dragon.generator :as gen]
            [dragon.util :as util]
            [dragon.web :as web]
            [mx.roads.forgotten.blog.email.content :as email-content]
            [mx.roads.forgotten.blog.routes :refer [gen-routes routes]]
            [mx.roads.forgotten.blog.social.content :as social-content]
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
  (let [base-path (config/base-path)
        posts-path (config/posts-path)
        posts (blog/process posts-path)
        generated-routes (gen-routes base-path
                                     posts-path
                                     posts)]
    (gen/run generated-routes
             (config/output-dir))
    (email-content/gen base-path posts)
    (social-content/gen base-path posts)))

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
