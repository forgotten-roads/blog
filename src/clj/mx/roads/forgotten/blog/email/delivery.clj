(ns mx.roads.forgotten.blog.email.delivery
  (:require
    [clojure.java.io :as io]
    [mx.roads.forgotten.blog.email.content :as email-content]
    [mx.roads.forgotten.blog.util :as util]
    [postal.core :as postal]
    [taoensso.timbre :as log]
    [trifl.fs :as fs]))

(def to "FRMX New Post Announcements <frmx-new-posts@googlegroups.com>")
(def from "FRMX Blog Updates <blog-updates@forgotten.roads.mx>")

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
  (let [post-data (util/get-post-data post-file)
        msg-file (util/get-message-content-file
                   post-file email-content/new-post-file)
        msg-content (slurp msg-file)]
    (send-message (:title post-data) msg-content)))
