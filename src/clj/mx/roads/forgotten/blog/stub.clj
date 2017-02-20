(ns mx.roads.forgotten.blog.stub
  (:require [clojure.pprint :refer [pprint]]
            [clojure.java.io :as io]
            [clojusc.twig :as logger]
            [mx.roads.forgotten.blog.config :as config]
            [mx.roads.forgotten.blog.meta :as meta]
            [mx.roads.forgotten.blog.util :as util]
            [taoensso.timbre :as log]))

(def stub-metadata
  "{:title \"REQUIRED\"
 :subtitle \"\"
 :excerpt \"\"
 :author \"REQUIRED\"
 :category \"REQUIRED\"
 :tags []
 :comment-link \"\"}\n")

(def rfc5322-content
  "TBD")

(defn stub-content
  [content-type]
  (case content-type
    :md ""
    :html ""
    :clj "(defn content [] \"REQUIRED\")"
    :edn "{:content \"REQUIRED\"}"
    :rfc5322 rfc5322-content))

(defn write-content
  [path content-type]
  (with-open [writer (io/writer (format
                                  "%s/content.%s" path (name content-type)))]
    (.write writer (stub-content content-type))))

(defn write-edn
  [path]
  (with-open [writer (io/writer (format "%s/meta.edn" path))]
    (.write writer stub-metadata)))

(defn write-files
  [{ym :ym dt :dt} content-type]
  (let [path (format "posts/%s/%s" ym dt)]
    (log/infof "Writing content and metadata files to %s ..." path)
    (io/make-parents (str path "/child"))
    (write-content path content-type)
    (write-edn path)))

(defn make-markdown-post
  [date]
  (write-files date :md))

(defn make-clojure-post
  [date]
  (write-files date :clj))

(defn make-edn-post
  [date]
  (write-files date :edn))

(defn make-html-post
  [date]
  (write-files date :html))

(defn make-rfc5322-post
  [date]
  (write-files date :rfc5322))
