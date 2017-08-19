(ns mx.roads.forgotten.blog.web.content.page
  (:require [dragon.web.content :as content]
            [mx.roads.forgotten.blog.web.content.data :as data]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Static Pages   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn about
  [posts]
  (content/render
    "templates/pages/generic.html"
    (data/about posts)))

(defn community
  [posts]
  (content/render
    "templates/pages/generic.html"
    (data/community posts)))

(defn contact
  [posts]
  (content/render
    "templates/pages/generic.html"
    (data/contact posts)))

(defn powered-by
  [posts]
  (content/render
    "templates/pages/generic.html"
    (data/powered-by posts)))

(defn license
  [posts]
  (content/render
    "templates/pages/generic.html"
    (data/license posts)))

(defn privacy
  [posts]
  (content/render
    "templates/pages/generic.html"
    (data/privacy posts)))

(defn disclosure
  [posts]
  (content/render
    "templates/pages/generic.html"
    (data/disclosure posts)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Dynamic Pages   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn post
  [posts post-data]
  (content/render
    "templates/pages/post.html"
    (data/post posts post-data)))

(defn front-page
  [posts]
  (let [above-fold 5
        below-fold 5
        headline-posts (take (+ above-fold below-fold) posts)]
    (content/render
      "templates/pages/home.html"
      (data/front-page
        posts
        headline-posts
        :above-fold-count above-fold
        :below-fold-count below-fold
        :column-count 2))))

(defn maps-index
  [posts maps-data]
  (content/render
    "templates/pages/maps.html"
    (data/maps-index
      posts maps-data)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Map Pages   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn map-fullscreen
  [map-data]
  (content/render
    "templates/maps/fullscreen.html"
    (data/map-minimal
      map-data)))

(defn map-kml-fullscreen
  [map-data]
  (content/render
    "templates/maps/kml-fullscreen.html"
    (data/map-minimal
      map-data)))

(defn map-kml-wide-page
  [posts map-data]
  (content/render
    "templates/maps/kml-wide-page.html"
    (data/map-common
      posts map-data)))

(defn map-kml-content-page
  [posts map-data]
  (content/render
    "templates/maps/kml-content-page.html"
    (data/map-common
      posts map-data)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Listings Pages   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn archives
  [posts]
  (content/render
    "templates/listings/archives.html"
    (data/archives posts)))

(defn categories
  [posts]
  (content/render
    "templates/listings/categories.html"
    (data/categories posts)))

(defn tags
  [posts]
  (content/render
    "templates/listings/tags.html"
    (data/tags posts)))

(defn authors
  [posts]
  (content/render
    "templates/listings/authors.html"
    (data/authors posts)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Design Pages   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn design
  [posts]
  (content/render
    "templates/design/main.html"
    (data/design posts)))

(defn bootstrap-theme
  [posts]
  (content/render
    "templates/design/bootstrap-theme.html"
    (data/design posts)))

(defn front-page-example
  [posts]
  (content/render
    "templates/design/front-page-example.html"
    (data/design posts)))

(defn blog-example
  [posts]
  (content/render
    "templates/design/blog-example.html"
    (data/design posts)))

(defn font-samples
  [posts]
  (content/render
    "templates/design/font-samples.html"
    (data/design posts)))

; (defn post-example
;   [posts]
;   (content/render
;     "templates/design/post-example.html"
;     (data/design posts)))
