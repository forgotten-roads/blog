(ns mx.roads.forgotten.blog.routes
  "The routes for the blog need to take into consideration the following:

   * Actual posts will be generated behind the scenes when processing on-disk
     content (e.g., when calling `process-all-by-year-and-month`).
   * The routes are only used durng development, when serving content
     dynamically.
   * Since the posts have already been generated and saved to disc, their
     routes should be generated dynamically as URI path / slurp call pairs."
  (:require [clojusc.twig :refer [pprint]]
            [dragon.blog :as blog]
            [dragon.config :as config]
            [mx.roads.forgotten.blog.maps :as maps]
            [mx.roads.forgotten.blog.reader :as reader]
            [mx.roads.forgotten.blog.sitemapper :as sitemapper]
            [mx.roads.forgotten.blog.web.content.page :as page]
            [taoensso.timbre :as log]))

(defn static-routes
  ([posts]
    (static-routes posts {}))
  ([posts routes]
    (merge
      routes
      {"/blog/about.html" (page/about posts)
       "/blog/contact.html" (page/contact posts)
       "/blog/powered-by.html" (page/powered-by posts)
       "/blog/license.html" (page/license posts)
       "/blog/privacy.html" (page/privacy posts)
       "/blog/disclosure.html" (page/disclosure posts)})))

(defn design-routes
  [posts routes]
  (merge
    routes
    {"/blog/design/index.html" (page/design posts)
     "/blog/design/bootstrap-theme.html" (page/bootstrap-theme posts)
     "/blog/design/example-front-page.html" (page/front-page-example posts)
     "/blog/design/example-blog.html" (page/blog-example posts)
     "/blog/design/map-simple.html" (page/map-simple-example posts)
     "/blog/design/map-kml-layer.html" (page/map-kml-example posts)}))

(defn post-routes
  [uri-base posts routes]
  (merge
    routes
    (blog/get-indexed-archive-routes
      (map vector (iterate inc 0) posts)
      :gen-func (partial page/post posts)
      :uri-base uri-base)))

(defn map-routes
  [uri-base routes]
  (merge
    routes
    (maps/get-map-routes
      :gen-funcs [
        #'page/map-fullscreen
        #'page/map-wide-page
        #'page/map-content-page]
      :uri-base uri-base)))

(defn index-routes
  [posts routes]
  (merge
    routes
    {"/blog/index.html" (page/front-page posts)
     "/blog/archives/index.html" (page/archives posts)
     "/blog/categories/index.html" (page/categories posts)
     "/blog/tags/index.html" (page/tags posts)
     "/blog/authors/index.html" (page/authors posts)}))

(defn reader-routes
  [uri-base posts routes]
  (let [route "/blog/atom.xml"]
    (merge
      routes
      {route (reader/atom-feed
               uri-base route (take (config/feed-count) posts))})))

(defn sitemaps-routes
  [uri-base routes]
  (let [route "/blog/sitemap.xml"]
    (merge
      routes
      {route (sitemapper/gen
               uri-base routes)})))

(defn routes
  [uri-base]
  (let [posts (blog/process uri-base)]
    (log/trace "Got data:" (pprint (blog/data-minus-body posts)))
    (->> (static-routes posts)
         (design-routes posts)
         (post-routes uri-base posts)
         (index-routes posts)
         (reader-routes uri-base posts)
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
  (let [posts (blog/process uri-base)]
    (log/trace "Got data:" (pprint (blog/data-minus-body posts)))
    (->> (gen-static-routes posts)
         (gen-design-routes posts)
         (gen-post-routes uri-base posts)
         (gen-index-routes posts)
         (gen-reader-routes uri-base posts)
         (gen-sitemaps-routes uri-base))))
