(ns mx.roads.forgotten.blog.routes
  "The routes for the blog need to take into consideration the following:

   * Actual posts will be generated behind the scenes when processing on-disk
     content (e.g., when calling `process-all-by-year-and-month`).
   * The routes are only used durng development, when serving content
     dynamically.
   * Since the posts have already been generated and saved to disc, their
     routes should be generated dynamically as URI path / slurp call pairs."
  (:require [clojusc.twig :refer [pprint]]
            [dragon.blog.core :as blog]
            [dragon.config :as config]
            [dragon.event.system.core :as event]
            [dragon.event.tag :as tag]
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
     "/blog/design/font-samples.html" (page/font-samples posts)}))

(defn post-routes
  [uri-posts posts routes]
  (merge
    routes
    (blog/get-indexed-archive-routes
      (map vector (iterate inc 0) posts)
      :gen-func (partial page/post posts)
      :uri-base uri-posts)))

(defn map-routes
  [uri-base posts routes]
  (let [view-data (maps/get-view-data uri-base)
        view-data-ui (maps/get-view-data-keep-ui uri-base)
        gen-data [[#'page/map-kml-fullscreen "fullscreen"]
                  [(partial page/map-kml-wide-page posts) "wide-page"]
                  [(partial page/map-kml-content-page posts) "content-page"]]]
    (merge
      routes
      {"/blog/map/ui/fullscreen.html" (page/map-fullscreen view-data-ui)
       "/blog/map/no-ui/fullscreen.html" (page/map-fullscreen view-data)
       "/blog/maps/index.html" (page/maps-index
                                 posts
                                 (maps/get-maps-data
                                   :gen-data gen-data
                                   :uri-base uri-base))}
      (maps/get-map-routes
        :gen-data gen-data
        :uri-base uri-base))))

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
  [uri-posts posts routes]
  (let [route "/blog/atom.xml"]
    (merge
      routes
      {route (reader/atom-feed
               uri-posts route (take (config/feed-count) posts))})))

(defn sitemaps-routes
  [routes]
  (let [route "/blog/sitemap.xml"]
    (merge
      routes
      {route (sitemapper/gen routes)})))

(defn routes
  [system uri-base uri-posts posts]
  (log/trace "Got data:" (pprint (blog/data-minus-body system posts)))
  (event/publish system tag/generate-routes-pre)
  (->> (static-routes posts)
       (design-routes posts)
       (post-routes uri-posts posts)
       (map-routes uri-base posts)
       (index-routes posts)
       (reader-routes uri-posts posts)
       (sitemaps-routes)
       vec
       (event/publish->> system tag/generate-routes-post)))

;;; Generator routes

(defn gen-route
  [func msg & args]
  (log/info msg)
  (apply func args))

(def gen-static-routes
  (partial
    gen-route
    static-routes
    "\tGenerating pages for static pages ..."))

(def gen-design-routes
  (partial
    gen-route
    design-routes
    "\tGenerating pages for design pages ..."))

(def gen-post-routes
  (partial
    gen-route
    post-routes
    "\tGenerating pages for blog posts ..."))

(def gen-map-routes
  (partial
    gen-route
    map-routes
    "\tGenerating pages for maps ..."))

(def gen-index-routes
  (partial
    gen-route
    index-routes
    "\tGenerating pages for front page, archives, categories, etc. ..."))

(def gen-reader-routes
  (partial
    gen-route
    reader-routes
    "\tGenerating XML for feeds ..."))

(def gen-sitemaps-routes
  (partial
    gen-route
    sitemaps-routes
    "\tGenerating XML for sitemap ..."))

(defn gen-routes
  [system uri-base uri-posts posts]
  (log/info "Generating routes ...")
  (log/trace "Got data:" (pprint (blog/data-minus-body system posts)))
  (event/publish system tag/generate-routes-pre)
  (->> (gen-static-routes posts)
       (gen-design-routes posts)
       (gen-post-routes uri-posts posts)
       (gen-map-routes uri-base posts)
       (gen-index-routes posts)
       (gen-reader-routes uri-posts posts)
       (gen-sitemaps-routes)
       vec
       (event/publish->> system tag/generate-routes-post)))
