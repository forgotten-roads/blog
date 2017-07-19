(ns mx.roads.forgotten.blog.maps
  (:require
    [clojure.math.combinatorics :refer [cartesian-product]]
    [clojure.java.io :as io]
    [clojure.string :as string]
    [mx.roads.forgotten.blog.util :as util]))

(def default-data-path "data/gis/")
(def kml-extension ".kml")
(def html-extension ".html")

(defn get-kml-files
  ([]
    (get-kml-files default-data-path))
  ([data-path]
    (->> data-path
         (io/resource)
         (io/file)
         (file-seq)
         (map str)
         (filter #(string/ends-with? % kml-extension))
         (map #(last (string/split % (re-pattern default-data-path)))))))

(defn get-map-paths
  [kml-files]
  (map #(first (string/split % (re-pattern kml-extension)))
       kml-files))

(defn get-html-files
  [kml-files]
  (map #(string/replace % (re-pattern kml-extension) html-extension)
       kml-files))

(defn get-map-route
  [[gem-func map-type] [kml map-path] uri-base]
  [(format "%s/maps/%s/%s.html" uri-base map-path map-type)
   (gem-func {:view-type map-type
              :kml-file kml
              :map-path map-path})])

(defn get-map-routes
  [& {:keys [gen-data uri-base]}]
  (let [kmls (get-kml-files)
        paths (get-map-paths kmls)
        map-data (util/zip kmls paths)]
    (->> (cartesian-product gen-data map-data [uri-base])
         (map #(apply get-map-route %))
         (into {}))))
