(ns mx.roads.forgotten.blog.web.content.data
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [dragon.blog.core :as blog]
            [dragon.blog.tags :as blog-tags]
            [dragon.config :as config]
            [markdown.core :as markdown]
            [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Helper Functions & Data Helpers   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def legal-block-extensions
  #{".selmer-block"})

(def legal-block-names
  #{"pre-css"
    "pre-head-scripts"
    "post-head-scripts"
    "head-postpends"
    "post-post-scripts"
    "article-body-ads"
    "article-sidebar-comments-links"})

(defn legal-block-file?
  [^java.io.File file]
  (->> legal-block-extensions
       (map #(string/ends-with? (.getCanonicalPath file) %))
       (remove false?)
       (not-empty)))

(defn legal-block-name?
  [block-name]
  (contains? legal-block-names block-name))

(defn block-matches?
  [block-file block-name]
  (and (legal-block-name? block-name)
       (string/includes? (str block-file) block-name)))

(defn get-block-name
  "Given a file object, "
  [^java.io.File block-file]
  (->> legal-block-names
       (filter (partial block-matches? block-file))
       (first)))

(defn get-block-files
  [parent-dir]
  (->> parent-dir
       (io/file)
       (.listFiles)
       (filter legal-block-file?)))

(defn get-block
  [block-file]
  (let [block-name (get-block-name block-file)]
    (if (nil? block-name)
      []
      [(keyword block-name) (slurp block-file)])))

(defn get-blocks
  [post-data]
  (->> post-data
       :src-dir
       (get-block-files)
       (map get-block)
       (into {})))

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
  [system]
  {:page-data {
     :base-path "/blog"
     :site-title (config/name system)
     :site-description (config/description system)
     :index "index"
     :about "about"
     :community "community"
     :archives "archives"
     :categories "categories"
     :tags "tags"
     :authors "authors"
     :active nil}})

(defn common
  ([system]
    (common {}))
  ([system posts]
    (assoc (base system)
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
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "about.md"
                          (markdown-page)
                          (assoc :title "About")))))

(defn community
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "community")
      (assoc :content (assoc generic-page :title "Community"))))

(defn contact
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "contact.md"
                          (markdown-page)
                          (assoc :title "Contact Us")))))

(defn powered-by
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "powered-by.md"
                          (markdown-page)
                          (assoc :title "Powered By")))))

(defn license
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "license.md"
                          (markdown-page)
                          (assoc :title "Content License")))))

(defn privacy
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "privacy.md"
                          (markdown-page)
                          (assoc :title "Privacy Policy")))))

(defn disclosure
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "disclosure.md"
                          (markdown-page)
                          (assoc :title "Disclosure Policy")))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Dynamic Pages Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn post
  [system posts post-data]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "archives")
      (assoc :post-data post-data
             :blocks (get-blocks post-data)
             :tags (blog-tags/unique [post-data]))))

(defn front-page
  [system all-posts top-posts & {:keys [above-fold-count below-fold-count column-count]}]
  (let [above-posts (take above-fold-count top-posts)
        headliner (first above-posts)
        grouped-posts (partition column-count
                                 (nthrest above-posts 1))
        below-posts (nthrest top-posts above-fold-count)]
    (-> system
        (common all-posts)
        (assoc-in [:page-data :active] "index")
        (assoc :headlines-heading "Headlines"
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
               :below-fold-data below-posts))))

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
  [system map-data]
  (-> (base system)
      (assoc-in [:page-data :active] "maps")
      (assoc :map-data (merge map-base map-data))))

(defn map-common
  [system posts map-data]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "maps")
      (assoc :map-data (merge map-base map-data))))

(defn maps-index
  [system posts maps-data]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "maps")
      (assoc :topo-data topo-base)
      (assoc :maps-data (map #(merge map-base %) maps-data))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Listings Pages   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn archives
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "archives")
      (assoc :content (assoc generic-page :title "Archives")
             :posts-data (blog/group-data :archives posts))))

(defn categories
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "categories")
      (assoc :content (assoc generic-page :title "Categories")
             :posts-data (blog/group-data :categories posts))))

(defn tags
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "tags")
      (assoc :content (assoc generic-page :title "Tags")
             :posts-data (blog/group-data :tags posts))))

(defn authors
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "authors")
      (assoc :content (assoc generic-page :title "Authors")
             :posts-data (blog/group-data :authors posts))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Design Pages Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn design
  [system posts]
  (-> system
      (common posts)
      (assoc-in [:page-data :active] "design")
      (assoc :content (assoc generic-page :title "Design"))))
