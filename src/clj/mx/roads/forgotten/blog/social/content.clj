(ns mx.roads.forgotten.blog.social.content
  (:require [clojure.java.io :as io]
            [clojusc.twig :refer [pprint]]
            [dragon.blog :as blog]
            [dragon.web.content :as content]
            [mx.roads.forgotten.blog.web.content.data :as data]
            [taoensso.timbre :as log]
            [trifl.fs :as fs]))

(def new-post-file "new-post.txt")

(defn get-content-filename
  [post-data filename]
  (format "%s/%s" (:src-dir post-data) filename))

(defn get-new-post-filename
  [post-data]
  (get-content-filename post-data new-post-file))

(defn get-new-post-social-content
  [post-data]
  (content/render
    (str "templates/social/" new-post-file)
    (data/post [] post-data)))

(defn gen-new-post-social
  [post-data]
  (let [file-data (get-new-post-social-content post-data)
        outfile (get-new-post-filename post-data)]
    (when-not (fs/file-exists? (io/file outfile))
      (log/debug "Generating 'new post' social content for" post-data)
      (spit
        outfile
        (get-new-post-social-content post-data)))))

(defn gen
  [base-path posts]
  (log/debug "Generating social content ...")
  (log/trace "Got data:" (pprint (blog/data-minus-body posts)))
  (doseq [post-data posts]
    (gen-new-post-social post-data))
  :ok)

(comment
  (require '[mx.roads.forgotten.blog.social.content :as social-content])
  (social-content/gen (config/base-path) (blog/process (config/posts-path))))
