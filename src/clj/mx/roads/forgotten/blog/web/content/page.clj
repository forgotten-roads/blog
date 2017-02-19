(ns mx.roads.forgotten.blog.web.content.page
  (:require [mx.roads.forgotten.blog.web.content.data :as data]
            [selmer.parser :as selmer]))

(defn front-page
  ([]
    (front-page {}))
  ([req]
    (selmer/render-file
      "templates/front-page.html"
      (data/index req))))

(defn about
  ([]
    (about {}))
  ([req]
    (selmer/render-file
      "templates/about.html"
      (data/about req))))