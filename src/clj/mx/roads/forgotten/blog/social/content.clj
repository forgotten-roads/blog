(ns mx.roads.forgotten.blog.social.content
  (:require [clojure.java.io :as io]
            [clojure.data.generators :as generators]
            [clojusc.twig :refer [pprint]]
            [dragon.blog :as blog]
            [dragon.content.core :as content]
            [dragon.web.content :as template]
            [mx.roads.forgotten.blog.web.content.data :as data]
            [taoensso.timbre :as log]
            [trifl.fs :as fs]))

(def new-post-file "new-post.txt")

(defn phrases
  [author category]
  {(format "A new FRMX post by %s:" author) 1
   "Have you seen the latest post on the Forgotten Roads blog?" 1
   "New blog post:" 1
   "There's a new FRMX post!" 1
   "We have a new Forgotten Roads post available:" 2
   "We've put a new Forgotten Roads post up on the blog:" 3
   "There's a new Forgotten Roads blog post:" 3
   (format "%s's written a new post for the blog:" author) 4
   "A new Forgotten Roads post has been published:" 4
   (format "We've got a new '%s' post available:" category) 6})

(defn get-phrase
  [author category]
  (generators/weighted (phrases author category)))

(defn get-content-filename
  [post-data filename]
  (format "%s/%s" (:src-dir post-data) filename))

(defn get-new-post-filename
  [post-data]
  (get-content-filename post-data new-post-file))

(defn render-template
  [post-data]
  (template/render
    (str "templates/social/" new-post-file)
    (data/post [] post-data)))

(defn get-new-post-social-content
  [post-data]
  (str (get-phrase (:author post-data) (:category post-data))
       (render-template post-data)))

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
