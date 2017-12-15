(ns mx.roads.forgotten.blog.web.content.data
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]
    [dragon.blog.content.block :as block]
    [dragon.blog.content.data :as page-data]
    [dragon.blog.core :as blog]
    [dragon.blog.tags :as blog-tags]
    [dragon.config.core :as config]
    [markdown.core :as markdown]
    [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constants & Helper Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def legal-block-names
  (block/legal-block-names
   #{"article-body-ads"
     "article-sidebar-comments-links"}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Base Data Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn common
  ([system]
    (common system {}))
  ([system posts]
    (common system posts {}))
  ([system posts additional-opts]
    (let [base-opts {:site-title (config/name system)
                     :site-description (config/description system)}]
      (page-data/common posts (merge base-opts additional-opts)))))

(defn about-opts
  [opts]
  (page-data/default-markdown-content-opts
    (assoc opts :category-key :about)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Static Pages Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn about
  [system posts]
  (common system
          posts
          (about-opts
            {:title "About"
             :content-filename "about.md"})))

(defn contact
  [system posts]
  (common system
          posts
          (about-opts
            {:title "Contact Us"
             :content-filename "contact.md"})))

(defn powered-by
  [system posts]
  (common system
          posts
          (about-opts
            {:title "Powered By"
             :content-filename "powered-by.md"})))

(defn license
  [system posts]
  (common system
          posts
          (about-opts
            {:title "Content License"
             :content-filename "license.md"})))

(defn privacy
  [system posts]
  (common system
          posts
          (about-opts
            {:title "Privacy Policy"
             :content-filename "privacy.md"})))

(defn disclosure
  [system posts]
  (common system
          posts
          (about-opts
            {:title "Disclosure Policy"
             :content-filename "disclosure.md"})))

(defn community
  [system posts]
  (let [data-content {}]
    (common system
            posts
            (page-data/default-data-content-opts
              {:title "Community"
               :category-key :community}))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Dynamic Pages Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn post
  [system posts post-data]
  (common system
          posts
          {:category-key "archives"
           :post-data post-data
           :blocks (block/get-blocks legal-block-names post-data)
           :tags (blog-tags/unique [post-data])}))

(defn front-page
  [system all-posts top-posts &
   {:keys [above-fold-count below-fold-count column-count]}]
  (let [above-posts (take above-fold-count top-posts)
        headliner (first above-posts)
        grouped-posts (partition column-count
                                 (nthrest above-posts 1))
        below-posts (nthrest top-posts above-fold-count)]
    (common system
            all-posts
            {:category-key "index"
             :headlines-heading "Headlines"
             :headlines-desc (str "We like to keep things simple at FRMX. "
                                  "Only the most recent headlines are kept "
                                  "on the front page -- if you want to read "
                                  "an older post, <a href=\"/blog/archives\""
                                  ">check out the archives</a>.")
             :tags (blog-tags/get-stats all-posts)
             :headliner headliner
             :posts-data grouped-posts
             :posts-count (count top-posts)
             :above-count (count above-posts)
             :below-count (count below-posts)
             :below-fold-data below-posts})))

(def map-base
  {:google-endpoint "https://maps.googleapis.com/maps/api/js"
   :google-api-key "AIzaSyCnCHagOgpmmE11nTCf9k99gZq6a3aLgnw"
   :starting-lat 43.536389
   :starting-long -96.731667
   :starting-zoom 12
   :disable-map-gui true})

(def topo-base
  (-> map-base
      (assoc :starting-zoom 13)
      (dissoc :disable-map-gui)))

(defn map-minimal
  [_system map-data]
  (page-data/base
    {:category-key "maps"
     :map-data (merge map-base map-data)}))

(defn map-common
  [system posts map-data]
  (common system
          posts
          {:category-key "maps"
           :map-data (merge map-base map-data)}))

(defn maps-index
  [system posts maps-data]
  (common system
          posts
          {:category-key "maps"
           :topo-data topo-base
           :maps-data (map #(merge map-base %) maps-data)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Listings Pages   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn archives
  [system posts]
  (common system
          posts
          (page-data/default-data-content-opts
            {:title "Archives"
             :category-key "archives"
             :posts-data (blog/group-data :archives posts)})))

(defn categories
  [system posts]
  (common system
          posts
          (page-data/default-data-content-opts
            {:title "Categories"
             :category-key "categories"
             :posts-data (blog/group-data :categories posts)})))

(defn tags
  [system posts]
  (common system
          posts
          (page-data/default-data-content-opts
            {:title "Tags"
             :category-key "tags"
             :posts-data (blog/group-data :tags posts)})))

(defn authors
  [system posts]
  (common system
          posts
          (page-data/default-data-content-opts
            {:title "Authors"
             :category-key "authors"
             :posts-data (blog/group-data :authors posts)})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Design Pages Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn design
  [system posts]
  (common system
          posts
          (page-data/default-data-content-opts
            {:title "Design"
             :category-key "design"})))
