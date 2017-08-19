(ns mx.roads.forgotten.blog.email.content
  (:require [clojure.java.io :as io]
            [clojusc.twig :refer [pprint]]
            [dragon.blog :as blog]
            [dragon.web.content :as content]
            [mx.roads.forgotten.blog.web.content.data :as data]
            [taoensso.timbre :as log]
            [trifl.fs :as fs]))

(def new-post-file "new-post.html")

(defn get-filename
  [post-data file-name]
  (format "%s/%s" (:src-dir post-data) file-name))

(defn get-new-post-filename
  [post-data]
  (get-filename post-data new-post-file))

(defn get-new-post-email-content
  [post-data]
  (content/render
    (str "templates/emails/" new-post-file)
    (data/post [] post-data)))

(defn gen-new-post-email
  [post-data]
  (let [file-data (get-new-post-email-content post-data)
        outfile (get-new-post-filename post-data)]
    (when-not (fs/file-exists? (io/file outfile))
      (log/debug "Generating 'new post' email content for" post-data)
      (spit
        outfile
        (get-new-post-email-content post-data)))))

(defn gen
  [base-path posts]
  (log/debug "Generating emails ...")
  (log/trace "Got data:" (pprint (blog/data-minus-body posts)))
  (doseq [post-data posts]
    (gen-new-post-email post-data))
  :ok)

(comment
  (require '[[mx.roads.forgotten.blog.email.content :as email-content] :as email-content])
  (email-content/gen (config/base-path) (blog/process (config/posts-path))))
