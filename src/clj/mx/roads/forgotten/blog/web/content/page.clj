(ns mx.roads.forgotten.blog.web.content.page
  (:require [mx.roads.forgotten.blog.web.content.data :as data]
            [dragon.web.content :as content]))

(defn about
  [posts]
  (content/render
    "templates/generic.html"
    (data/about posts)))

(defn powered-by
  [posts]
  (content/render
    "templates/generic.html"
    (data/powered-by posts)))

(defn community
  [posts]
  (content/render
    "templates/community.html"
    (data/community posts)))

(defn post
  [posts post-data]
  (content/render
    "templates/post.html"
    (data/post posts post-data)))

(defn front-page
  [posts]
  (content/render
    "templates/front-page.html"
    (data/front-page
      posts
      :post-count 5
      :column-count 2)))

(defn archives
  [posts]
  (content/render
    "templates/archives.html"
    (data/archives posts)))

(defn categories
  [posts]
  (content/render
    "templates/categories.html"
    (data/categories posts)))

(defn tags
  [posts]
  (content/render
    "templates/tags.html"
    (data/tags posts)))

(defn authors
  [posts]
  (content/render
    "templates/authors.html"
    (data/authors posts)))

(defn design
  [posts]
  (content/render
    "templates/design.html"
    (data/design posts)))

(defn bootstrap-theme
  [posts]
  (content/render
    "templates/bootstrap-theme.html"
    (data/design posts)))

(defn front-page-example
  [posts]
  (content/render
    "templates/front-page-example.html"
    (data/design posts)))

(defn blog-example
  [posts]
  (content/render
    "templates/blog-example.html"
    (data/design posts)))

; (defn post-example
;   [posts]
;   (content/render
;     "templates/post-example.html"
;     (data/design posts)))
