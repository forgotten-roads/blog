(ns mx.roads.forgotten.blog.email.delivery
  (:require [clojure.java.io :as io]
            [dragon.content.core :as content]
            [mx.roads.forgotten.blog.email.content :as email-content]
            [postal.core :as postal]
            [taoensso.timbre :as log]
            [trifl.fs :as fs]))

(def to "FRMX New Post Announcements <frmx-new-posts@googlegroups.com>")
(def from "FRMX Blog Updates <blog-updates@forgotten.roads.mx>")

(defn get-post-data
  [post-file]
  (log/debugf "Getting content for %s ..." post-file)
  (-> post-file
      (io/resource)
      (.getFile)
      (io/file)
      (content/parse)))

(defn get-message-content-file
  [post-file]
  (let [parent-dir (fs/parent (io/file post-file))]
    (->> email-content/new-post-file
         (format "%s/%s" parent-dir)
         (io/resource)
         (.getFile)
         (io/file))))

(defn make-message
  [to from subject content]
  {:from from
   :to to
   :subject subject
   :body [{:type "text/html; charset=utf-8"
           :content content}]})

(defn log-delivery-status
  [result]
  (log/infof "Message delivery status: %s - %s"
             (name (:error result))
             (:message result)))

(defn send-message
  [subject content]
  (log/infof "Sending message '%s' to %s ..." subject to)
  (->> content
       (make-message to from subject)
       (postal/send-message)
       (log-delivery-status))
  :ok)

(defn send-new-post-message
  [post-file]
  (let [post-data (get-post-data post-file)
        msg-file (get-message-content-file post-file)
        msg-content (slurp msg-file)]
    (send-message (:title post-data) msg-content)))

(comment
  (def posts (blog/process (config/posts-path)))
  (def file "posts/2017-08/17-202020/content.rfc5322")
  (def post-data (email-delivery/get-post-data file)))
