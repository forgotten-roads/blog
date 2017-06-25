(ns mx.roads.forgotten.blog.web.content.data
  (:require [clojure.java.io :as io]
            [dragon.blog :as blog]
            [dragon.config :as config]
            [markdown.core :as markdown]))

(defn posts-stats
  [posts]
  {:posts (count posts)
   :authors (->> posts
                 (map :author)
                 set
                 count)
   :words (->> posts
               (map :word-count)
               (reduce + 0))
   :chars (->> posts
               (map :char-count)
               (reduce + 0))})

(defn base
  ([]
    (base {}))
  ([posts]
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
                 :active nil}
      :posts-data posts
      :posts-stats (posts-stats posts)}))

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

(defn about
  [posts]
  (-> posts
      (base)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "about.md"
                          (markdown-page)
                          (assoc :title "About")))))

(defn powered-by
  [posts]
  (-> posts
      (base)
      (assoc-in [:page-data :active] "about")
      (assoc :content (-> "powered-by.md"
                          (markdown-page)
                          (assoc :title "Powered By")))))

(defn archives
  [posts]
  (-> posts
      (base)
      (assoc-in [:page-data :active] "archives")
      (assoc :content (assoc generic-page :title "Archives")
             :posts-data (blog/data-for-archives posts))))

(defn categories
  [posts]
  (-> posts
      (base)
      (assoc-in [:page-data :active] "categories")
      (assoc :content (assoc generic-page :title "Categories")
             :posts-data (blog/data-for-categories posts))))

(defn tags
  [posts]
  (-> posts
      (base)
      (assoc-in [:page-data :active] "tags")
      (assoc :content (assoc generic-page :title "Tags")
             :posts-data (blog/data-for-tags posts))))


(defn authors
  [posts]
  (-> posts
      (base)
      (assoc-in [:page-data :active] "authors")
      (assoc :content (assoc generic-page :title "Authors")
             :posts-data (blog/data-for-authors posts))))

(defn community
  [posts]
  (-> posts
      (base)
      (assoc-in [:page-data :active] "community")
      (assoc :content (assoc generic-page :title "Community"))))

(defn design
  [posts]
  (-> posts
      (base)
      (assoc-in [:page-data :active] "design")
      (assoc :content (assoc generic-page :title "Design"))))

(defn front-page
  [posts & {:keys [post-count column-count]}]
  (let [headliner (first posts)
        grouped-posts (partition column-count
                                 (take (dec post-count)
                                       (rest posts)))]
    (-> posts
        (base)
        (assoc-in [:page-data :active] "index")
        (assoc :tags (blog/tags posts)
               :headliner headliner
               :posts-data grouped-posts))))

(defn post
  [posts post-data]
  (-> posts
      (base)
      (assoc-in [:page-data :active] "archives")
      (assoc :post-data post-data
             :tags (blog/tags [post-data]))))
