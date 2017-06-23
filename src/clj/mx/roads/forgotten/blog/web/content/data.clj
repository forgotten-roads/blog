(ns mx.roads.forgotten.blog.web.content.data
  (:require [clojure.java.io :as io]
            [dragon.blog :as blog]
            [dragon.config :as config]
            [markdown.core :as markdown]))

(defn base
  ([]
    (base {}))
  ([data]
    (merge
      data
      {:base-path "/blog"
       :site-title (config/name)
       :site-description (config/description)
       :index "index"
       :about "about"
       :community "community"
       :archives "archives"
       :categories "categories"
       :tags "tags"
       :authors "authors"})))

(defn markdown-page
  [md-file]
  {:content (->> md-file
                 (str "markdown/")
                 (io/resource)
                 (slurp)
                 (markdown/md-to-html-string))})

(defn about
  []
  {:page-data
    (merge
      (base {:active "about"})
      (markdown-page "about.md"))})

(defn community
  []
  {:page-data (base {:active "community"})})

(defn design
  []
  {:page-data (base {:active "design"})})

(defn post
  [data]
  {:page-data (base {:active "archives"})
   :post-data data
   :tags (blog/tags [data])})

(defn archives
  [data]
  {:page-data (base {:active "archives"})
   :posts-data data})

(defn front-page
  [data & {post-count :post-count column-count :column-count}]
  (let [headliner (first data)
        posts (partition column-count (take (dec post-count) (rest data)))]
  {:page-data (base {:active "index"})
   :tags (blog/tags data)
   :headliner headliner
   :posts-data posts}))

(defn categories
  [data]
  {:page-data (base {:active "categories"})
   :posts-data data})

(defn tags
  [data]
  {:page-data (base {:active "tags"})
   :posts-data data})

(defn authors
  [data]
  {:page-data (base {:active "authors"})
   :posts-data data})
