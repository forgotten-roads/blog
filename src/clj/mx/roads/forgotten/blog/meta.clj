(ns mx.roads.forgotten.blog.meta
  (:require [clojure.pprint :refer [pprint]]
            [clojusc.twig :as logger]
            [mx.roads.forgotten.blog.util :as util]
            [taoensso.timbre :as log])
  (:refer-clojure :exclude [get]))

(defn get
  [post]
  post)

(defn get-all
  []
  ;; XXX get all posts
  ;; map get over all posts
  [])
