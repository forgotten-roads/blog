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
      {:base-path "/blog"
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
      data)))

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
  []
  {:page-data (base {:active "about"})
   :content (-> "about.md"
                (markdown-page)
                (assoc :title "About"))})

(defn archives
  [data]
  {:page-data (base {:active "archives"})
   :posts-data data
   :content (assoc generic-page :title "Archives")})

(defn categories
  [data]
  {:page-data (base {:active "categories"})
   :posts-data data
   :content (assoc generic-page :title "Categories")})

(defn tags
  [data]
  {:page-data (base {:active "tags"})
   :posts-data data
   :content (assoc generic-page :title "Tags")})

(defn authors
  [data]
  {:page-data (base {:active "authors"})
   :posts-data data
   :content (assoc generic-page :title "Authors")})

(defn community
  []
  {:page-data (base {:active "community"})
   :content (assoc generic-page :title "Community")})

(defn design
  []
  {:page-data (base {:active "design"})
   :content (assoc generic-page :title "Design")})

(defn front-page
  [data & {post-count :post-count column-count :column-count}]
  (let [headliner (first data)
        posts (partition column-count (take (dec post-count) (rest data)))]
  {:page-data (base {:active "index"})
   :tags (blog/tags data)
   :headliner headliner
   :posts-data posts}))

(defn post
  [data]
  {:page-data (base {:active "archives"})
   :post-data data
   :tags (blog/tags [data])})
