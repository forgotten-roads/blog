(ns mx.roads.forgotten.blog.web.content.page
  (:require [mx.roads.forgotten.blog.web.content.data :as data]
            [selmer.parser :as selmer]))

(defn front-page
  [req]
  (selmer/render-file
    "templates/front-page.html"
    (data/base-page req)))

(defn about
  [req]
  (selmer/render-file
    "templates/page.html"
    (data/base-page req)))
