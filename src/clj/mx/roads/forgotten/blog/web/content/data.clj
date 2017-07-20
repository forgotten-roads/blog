(ns mx.roads.forgotten.blog.web.content.data
  (:require [clojure.java.io :as io]
            [dragon.blog :as blog]
            [dragon.config :as config]
            [markdown.core :as markdown]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Helper Functions & Data Helpers   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn posts-stats
  [posts]
  {:posts (count posts)
   :authors (->> posts
                 (map :author)
                 set
                 count)
   :lines (->> posts
               (map :line-count)
               (reduce +))
   :words (->> posts
               (map :word-count)
               (reduce +))
   :chars (->> posts
               (map :char-count)
               (reduce +))})

(defn base
  []
  {:page-data {:base-path "/blog"
               :site-title (config/name)
               :site-description (config/description)
               :index "index"
               :about "about"
               :community "community"
               :archives "archives"
               :categories "categories"
               :tags "tags"
               :authors "authors"
               :active nil}})

(defn common
  ([]
    (common {}))
  ([posts]
    (assoc (base)
           :posts-data posts
           :posts-stats (posts-stats posts))))

(def generic-page
  {:title nil
   :subtitle nil})

(defn markdown-page
  [md-file]
  (merge
    generic-page
    {:body (->> md-file
                (str "markdown/")
                (io/resource)
                (slurp)
                (markdown/md-to-html-string))}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Static Pages Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn about
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "about.md"
                          (markdown-page)
                          (assoc :title "About")))))

(defn community
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "community")
      (assoc :content (assoc generic-page :title "Community"))))

(defn contact
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "contact.md"
                          (markdown-page)
                          (assoc :title "Contact Us")))))

(defn powered-by
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "powered-by.md"
                          (markdown-page)
                          (assoc :title "Powered By")))))

(defn license
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "license.md"
                          (markdown-page)
                          (assoc :title "Content License")))))

(defn privacy
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "privacy.md"
                          (markdown-page)
                          (assoc :title "Privacy Policy")))))

(defn disclosure
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "disclosure.md"
                          (markdown-page)
                          (assoc :title "Disclosure Policy")))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Dynamic Pages Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn post
  [posts post-data]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "archives")
      (assoc :post-data post-data
             :tags (blog/tags [post-data]))))

(defn front-page
  [posts & {:keys [post-count column-count]}]
  (let [headliner (first posts)
        grouped-posts (partition column-count
                                 (take (dec post-count)
                                       (rest posts)))]
    (-> posts
        (common)
        (assoc-in [:page-data :active] "index")
        (assoc :headlines-heading "Headlines"
               :headlines-desc (str "We like to keep things simple at FRMX. "
                                    "Only the most recent headlines are kept "
                                    "on the front page -- if you want to read "
                                    "an older post, <a href=\"/blog/archives\""
                                    ">check out the archives</a>.")
               :tags (blog/tags posts)
               :headliner headliner
               :posts-data grouped-posts))))

(defn map-minimal
  [map-data]
  (-> (base)
      (assoc-in [:page-data :active] "maps")
      (assoc :map-data map-data)))

(defn map-common
  [posts map-data]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "maps")
      (assoc :map-data map-data)))

(defn maps-index
  [posts maps-data]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "maps")
      (assoc :maps-data maps-data)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Listings Pages   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn archives
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "archives")
      (assoc :content (assoc generic-page :title "Archives")
             :posts-data (blog/data-for-archives posts))))

(defn categories
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "categories")
      (assoc :content (assoc generic-page :title "Categories")
             :posts-data (blog/data-for-categories posts))))

(defn tags
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "tags")
      (assoc :content (assoc generic-page :title "Tags")
             :posts-data (blog/data-for-tags posts))))

(defn authors
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "authors")
      (assoc :content (assoc generic-page :title "Authors")
             :posts-data (blog/data-for-authors posts))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Design Pages Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn design
  [posts]
  (-> posts
      (common)
      (assoc-in [:page-data :active] "design")
      (assoc :content (assoc generic-page :title "Design"))))



