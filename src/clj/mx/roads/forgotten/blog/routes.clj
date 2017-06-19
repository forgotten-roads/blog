(ns mx.roads.forgotten.blog.routes
  "The routes for the blog need to take into consideration the following:

   * Actual posts will be generated behind the scenes when processing on-disk
     content (e.g., when calling `process-all-by-year-and-month`).
   * The routes are only used durng development, when serving content
     dynamically.
   * Since the posts have already been generated and saved to disc, their
     routes should be generated dynamically as URI path / slurp call pairs.
  "
  (:require [clojusc.twig :refer [pprint]]
            [dragon.blog :as blog]
            [dragon.config :as config]
            [mx.roads.forgotten.blog.reader :as reader]
            [mx.roads.forgotten.blog.web.content.page :as page]
            [taoensso.timbre :as log]))

(defn static-routes
  []
  (log/info "Generating pages for static pages ...")
  {"/about.html" (page/about)})

(defn design-routes
  []
  (log/info "Generating pages for design pages ...")
  {"/design/index.html" (page/design)
   "/design/bootstrap-theme.html" (page/bootstrap-theme)
   "/design/example-blog.html" (page/blog-example)})

(defn post-routes
  [uri-base data]
  (log/info "Generating pages for blog posts ...")
  (blog/get-indexed-archive-routes
    (map vector (iterate inc 0) data)
    :gen-func page/post
    :uri-base uri-base))

(defn index-routes
  [data]
  (log/info "Generating pages for front page, archives, categories, etc. ...")
  {"/index.html" (page/front-page data)
   "/archives/index.html" (page/archives data)
   "/categories/index.html" (page/categories data)
   "/tags/index.html" (page/tags data)
   "/authors/index.html" (page/authors data)})

(defn reader-routes
  [uri-base data]
  (log/info "Generating XML for feeds ...")
  (let [route "/atom.xml"]
    {route (reader/atom-feed
             uri-base route (take (config/feed-count) data))}))

(defn routes
  [uri-base]
  (let [data (blog/process uri-base)]
    (log/trace "Got data:" (pprint (blog/data-minus-body data)))
    (merge
      (static-routes)
      (design-routes)
      (post-routes uri-base data)
      (index-routes data)
      (reader-routes uri-base data))))
