(ns mx.roads.forgotten.blog.sitemapper
  (:require [clojure.data.xml :as xml]
            [clojusc.twig :refer [pprint]]
            [dragon.config :as config]
            [dragon.util :as util]
            [taoensso.timbre :as log]))

(defn url
  [datestamp route]
  [:url
   [:loc (str "http://forgotten.roads.mx" route)]
   [:lastmod datestamp]
   [:changefreq "weekly"]])

(defn urlset
  [datestamp routes]
  [:urlset {:xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"}
   (map (partial url datestamp) (keys routes))])

(defn gen
  [routes]
  (let [datestamp (util/format-datestamp (util/now :datetime-map))]
    (xml/emit-str
     (xml/sexp-as-element
      (urlset datestamp routes)))))
