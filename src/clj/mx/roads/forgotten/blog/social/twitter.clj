(ns mx.roads.forgotten.blog.social.twitter
  (:require [clojure.java.io :as io]
            [dragon.content.core :as content]
            [mx.roads.forgotten.blog.social.content :as social-content]
            [mx.roads.forgotten.blog.util :as util]
            [taoensso.timbre :as log]
            [trifl.fs :as fs]
            [twitter.api.restful :as twitter]
            [twitter.oauth :as oath]))

(def screen-name "ForgottenRdsMX")

(defn get-app-consumer-key
  []
  (util/home-file->str "~/.twitter/frmx/app-consumer-key"))

(defn get-app-consumer-secret
  []
  (util/home-file->str "~/.twitter/frmx/app-consumer-secret"))

(defn get-user-access-token
  []
  (util/home-file->str "~/.twitter/frmx/user-access-token"))

(defn get-user-access-token-secret
  []
  (util/home-file->str "~/.twitter/frmx/user-access-token-secret"))

(defn get-creds
  []
  (oath/make-oauth-creds (get-app-consumer-key)
                         (get-app-consumer-secret)
                         (get-user-access-token)
                         (get-user-access-token-secret)))

(defn send-message
  [content]
  (twitter/statuses-update
    :oauth-creds (get-creds)
    :params {:status content}))

(defn send-new-post-message
  [post-file]
  (let [post-data (util/get-post-data post-file)
        msg-file (util/get-message-content-file
                   post-file social-content/new-post-file)
        msg-content (slurp msg-file)]
    (send-message msg-content)))

(defn show-friends
  []
  (twitter/friendships-show
    :oauth-creds (get-creds)
    :params {:target-screen-name screen-name}))
