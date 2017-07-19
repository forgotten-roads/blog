(ns mx.roads.forgotten.blog.reader
  (:require [clojure.data.xml :as xml]
            [clojusc.twig :refer [pprint]]
            [dragon.config :as config]
            [taoensso.timbre :as log]))

(defn atom-entry
  [uri-posts post]
  (let [uri (str uri-posts (:uri-path post))]
    [:entry
     [:title (:title post)]
     [:updated (:timestamp post)]
     [:author [:name (:author post)]]
     [:link {:href (format "http://%s/%s" (config/domain) uri)}]
     [:id (format "%s:feed:post:%s" (config/domain-urn) (:title post))]
     [:content {:type "html"} (:body post)]]))

(defn atom-feed
  [uri-posts route posts]
  (xml/emit-str
   (xml/sexp-as-element
    [:feed {:xmlns "http://www.w3.org/2005/Atom"}
     [:id (format "%s:feed" (config/domain-urn))]
     [:updated (-> posts first :timestamp)]
     [:title {:type "text"} (config/name)]
     [:link {:rel "self" :href (format "http://%s%s" (config/domain) route)}]
     (map (partial atom-entry uri-posts) posts)])))
