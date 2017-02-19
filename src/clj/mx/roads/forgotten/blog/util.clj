(ns mx.roads.forgotten.blog.util
  (:require [clojure.string :as string]
            [clojure.java.shell :as shell]
            [taoensso.timbre :as log]))

(defn get-metas
  ""
  [an-ns]
  (->> an-ns
       (ns-publics)
       (map (fn [[k v]] [k (meta v)]))
       (into {})))

(defn get-meta
  "Takes the same form as the general `get-in` function:

      (get-meta 'my.name.space ['my-func :doc])"
  [an-ns rest]
  (-> an-ns
      (get-metas)
      (get-in rest)))

(defn get-docstring
  ""
  [an-ns fn-name]
  (-> an-ns
      (get-meta [fn-name :doc])
      ;; strip markdown for code
      (string/replace #"`" "")))

(defn format-docstring
  [an-ns fn-name]
  (->> fn-name
       (get-docstring an-ns)
       (format "%s\n\n")))

(defn print-docstring
  [an-ns fn-name]
  (print (format-docstring an-ns fn-name)))

(defn get-build
  []
  (:out (shell/sh "git" "rev-parse" "--short" "HEAD")))
