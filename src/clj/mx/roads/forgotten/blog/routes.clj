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
  ([system posts]
    (static-routes system posts {}))
  ([system posts routes]
    (merge
      routes
      {"/blog/about.html" (page/about system posts)
       "/blog/contact.html" (page/contact system posts)
       "/blog/powered-by.html" (page/powered-by system posts)
       "/blog/license.html" (page/license system posts)
       "/blog/privacy.html" (page/privacy system posts)
       "/blog/disclosure.html" (page/disclosure system posts)})))

(defn design-routes
  [system posts routes]
  (merge
    routes
    {"/blog/design/index.html" (page/design system posts)
     "/blog/design/bootstrap-theme.html" (page/bootstrap-theme system posts)
     "/blog/design/example-front-page.html" (page/front-page-example system posts)
     "/blog/design/example-blog.html" (page/blog-example system posts)
     "/blog/design/font-samples.html" (page/font-samples system posts)}))

(defn post-routes
  [system posts routes]
  (merge
    routes
    (blog/get-indexed-archive-routes
      (map vector (iterate inc 0) posts)
      :gen-func (partial page/post system posts)
      :uri-base (config/posts-path system))))

(defn map-routes
  [system posts routes]
  (let [uri-base (config/base-path system)
        view-data (maps/get-view-data uri-base)
        view-data-ui (maps/get-view-data-keep-ui uri-base)
        gen-data [[(partial page/map-kml-fullscreen system)
                   "fullscreen"]
                  [(partial page/map-kml-wide-page system posts)
                   "wide-page"]
                  [(partial page/map-kml-content-page system posts)
                   "content-page"]]]
    (merge
      routes
      {"/blog/map/ui/fullscreen.html" (page/map-fullscreen
                                       system view-data-ui)
       "/blog/map/no-ui/fullscreen.html" (page/map-fullscreen
                                          system view-data)
       "/blog/maps/index.html" (page/maps-index
                                 system
                                 posts
                                 (maps/get-maps-data
                                   :gen-data gen-data
                                   :uri-base uri-base))}
      (maps/get-map-routes
        :gen-data gen-data
        :uri-base uri-base))))

(defn index-routes
  [system posts routes]
  (merge
    routes
    {"/blog/index.html" (page/front-page system posts)
     "/blog/archives/index.html" (page/archives system posts)
     "/blog/categories/index.html" (page/categories system posts)
     "/blog/tags/index.html" (page/tags system posts)
     "/blog/authors/index.html" (page/authors system posts)}))

(defn reader-routes
  [system posts routes]
  (let [route "/blog/atom.xml"]
    (merge
      routes
      {route (reader/atom-feed
               system
               route
               (take (config/feed-count system) posts))})))

(defn sitemaps-routes
  [system routes]
  (let [route "/blog/sitemap.xml"]
    (merge
      routes
      {route (sitemapper/gen routes)})))

(defn routes
  [system posts]
  (log/trace "Got data:" (pprint (blog/data-minus-body system posts)))
  (event/publish system tag/generate-routes-pre)
  (->> (static-routes system posts)
       (design-routes system posts)
       (post-routes system posts)
       (map-routes system posts)
       (index-routes system posts)
       (reader-routes system posts)
       (sitemaps-routes system)
       (event/publish->> system tag/generate-routes-post)
       vec))

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
  [system posts]
  (log/info "Generating routes ...")
  (log/trace "Got data:" (pprint (blog/data-minus-body system posts)))
  (event/publish system tag/generate-routes-pre)
  (->> (gen-static-routes system posts)
       (gen-design-routes system posts)
       (gen-post-routes system posts)
       (gen-map-routes system posts)
       (gen-index-routes system posts)
       (gen-reader-routes system posts)
       (gen-sitemaps-routes system)
       (event/publish->> system tag/generate-routes-post)
       vec))
