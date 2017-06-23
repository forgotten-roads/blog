(ns mx.roads.forgotten.blog.web.content.page
  (:require [mx.roads.forgotten.blog.web.content.data :as data]
            [dragon.blog :as blog]
            [dragon.web.content :as content]))

(defn about
  []
  (content/render
    "templates/generic.html"
    (data/about)))

(defn community
  []
  (content/render
    "templates/community.html"
    (data/community)))

(defn post
  [data]
  (content/render
    "templates/post.html"
    (data/post data)))

(defn front-page
  [data]
  (content/render
    "templates/front-page.html"
    (data/front-page
      data
      :post-count 5
      :column-count 2)))

(defn archives
  [data]
  (content/render
    "templates/archives.html"
    (-> data
        (blog/data-for-archives)
        (data/archives))))

(defn categories
  [data]
  (content/render
    "templates/categories.html"
    (-> data
        (blog/data-for-categories)
        (data/categories))))

(defn tags
  [data]
  (content/render
    "templates/tags.html"
    (-> data
        (blog/data-for-tags)
        (data/tags))))

(defn authors
  [data]
  (content/render
    "templates/authors.html"
    (-> data
        (blog/data-for-authors)
        (data/authors))))

(defn design
  []
  (content/render
    "templates/design.html"
    (data/design)))

(defn bootstrap-theme
  []
  (content/render
    "templates/bootstrap-theme.html"
    (data/design)))

(defn blog-example
  []
  (content/render
    "templates/blog-example.html"
    (data/design)))
