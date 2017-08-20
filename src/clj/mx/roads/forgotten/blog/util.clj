(ns mx.roads.forgotten.blog.util
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [dragon.content.core :as content]
            [taoensso.timbre :as log]
            [trifl.fs :as fs]))

(defn zip
  [& colls]
  (partition (count colls)
             (apply interleave colls)))

(defn get-post-data
  [post-file]
  (log/debugf "Getting content for %s ..." post-file)
  (-> post-file
      (io/resource)
      (.getFile)
      (io/file)
      (content/parse)))

(defn get-message-content-file
  [post-file msg-filename]
  (let [parent-dir (fs/parent (io/file post-file))]
    (->> msg-filename
         (format "%s/%s" parent-dir)
         (io/resource)
         (.getFile)
         (io/file))))
