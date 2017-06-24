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
            [mx.roads.forgotten.blog.sitemapper :as sitemapper]
            [mx.roads.forgotten.blog.web.content.page :as page]
            [taoensso.timbre :as log]))

(defn static-routes
  ([]
    (static-routes {}))
  ([routes]
    (merge
      routes
      {"/blog/about.html" (page/about)
       "/blog/powered-by.html" (page/powered-by)})))

(defn design-routes
  [routes]
  (merge
    routes
    {"/blog/design/index.html" (page/design)
     "/blog/design/bootstrap-theme.html" (page/bootstrap-theme)
     "/blog/design/example-blog.html" (page/blog-example)}))

(defn post-routes
  [uri-base data routes]
  (merge
    routes
    (blog/get-indexed-archive-routes
      (map vector (iterate inc 0) data)
      :gen-func page/post
      :uri-base uri-base)))

(defn index-routes
  [data routes]
  (merge
    routes
    {"/blog/index.html" (page/front-page data)
     "/blog/archives/index.html" (page/archives data)
     "/blog/categories/index.html" (page/categories data)
     "/blog/tags/index.html" (page/tags data)
     "/blog/authors/index.html" (page/authors data)}))

(defn reader-routes
  [uri-base data routes]
  (let [route "/blog/atom.xml"]
    (merge
      routes
      {route (reader/atom-feed
               uri-base route (take (config/feed-count) data))})))

(defn sitemaps-routes
  [uri-base routes]
  (let [route "/blog/sitemap.xml"]
    (merge
      routes
      {route (sitemapper/gen
               uri-base routes)})))

(defn routes
  [uri-base]
  (let [data (blog/process uri-base)]
    (log/trace "Got data:" (pprint (blog/data-minus-body data)))
    (->> (static-routes)
         (design-routes)
         (post-routes uri-base data)
         (index-routes data)
         (reader-routes uri-base data)
         (sitemaps-routes uri-base))))

;;; Generator routes

(defn gen-route
  [func msg & args]
  (log/info msg)
  (apply func args))

(def gen-static-routes
  (partial
    gen-route
    static-routes
    "Generating pages for static pages ..."))

(def gen-design-routes
  (partial
    gen-route
    design-routes
    "Generating pages for design pages ..."))

(def gen-post-routes
  (partial
    gen-route
    post-routes
    "Generating pages for blog posts ..."))

(def gen-index-routes
  (partial
    gen-route
    index-routes
    "Generating pages for front page, archives, categories, etc. ..."))

(def gen-reader-routes
  (partial
    gen-route
    reader-routes
    "Generating XML for feeds ..."))

(def gen-sitemaps-routes
  (partial
    gen-route
    sitemaps-routes
    "Generating XML for sitemap ..."))

(defn gen-routes
  [uri-base]
  (let [data (blog/process uri-base)]
    (log/trace "Got data:" (pprint (blog/data-minus-body data)))
    (->> (gen-static-routes)
         (gen-design-routes)
         (gen-post-routes uri-base data)
         (gen-index-routes data)
         (gen-reader-routes uri-base data)
         (gen-sitemaps-routes uri-base))))
