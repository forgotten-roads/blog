(ns mx.roads.forgotten.blog.maps
  (:require
    [clojure.math.combinatorics :refer [cartesian-product]]
    [clojure.java.io :as io]
    [clojure.string :as string]
    [mx.roads.forgotten.blog.util :as util]))

(def default-data-path "data/gis/")
(def map-style-file (str default-data-path "google-map-styles.json"))
(def kml-extension ".kml")
(def html-extension ".html")
(def revision 8)

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

(defn get-map-files
  []
  (let [kmls (get-kml-files)
        paths (get-map-paths kmls)]
    (util/zip kmls paths)))

(defn base-map-data
  [uri-base]
  (let [maps-base (format "%s/maps" uri-base)]
    {:uri-base uri-base
     :maps-base maps-base
     :revision revision
     :style-json (slurp (io/resource map-style-file))}))

(defn get-map-data
  [[_ map-type] [kml map-path] uri-base]
  (merge
    (base-map-data uri-base)
    {:view-type map-type
     :view-type-display (-> map-type
                            (string/capitalize)
                            (string/replace "-" " "))
     :kml-file kml
     :map-path map-path
     :html-file (format "%s/%s.html" map-path map-type)
     :gis-base (format "%s/%s" uri-base default-data-path)}))

(defn get-maps-data
  [& {:keys [gen-data uri-base]}]
  (->> uri-base
       (vector)
       (cartesian-product gen-data (get-map-files))
       (map #(apply get-map-data %))
       (vec)
       (sort-by :html-file)
       (group-by :map-path)
       (vec)
       (map #(zipmap [:key :data] %))
       (sort-by :key)))

(defn get-view-data
  [uri-base]
  (merge
    (base-map-data uri-base)
    ;; Note that currently `view-types` and `template-types` aren't used.
    {:view-types [
      {:name "No Map UI (clean view)"
       :path-segment "no-ui"}
      {:name "With Map UI"
       :path-segment "ui"}]
     :template-types [
      {:name "Content page"
       :path-segment "content-page"}
      {:name "Fullscreen"
       :path-segment "fullscreen"}
      {:name "Wide page"
       :path-segment "wide-page"}]
     :starting-zoom 6}))

(defn get-view-data-keep-ui
  [uri-base]
  (merge
    (get-view-data uri-base)
    {:disable-map-gui false}))

(defn get-map-route
  [[gen-func map-type] [kml map-path] uri-base]
  [(format "%s/maps/%s/%s.html" uri-base map-path map-type)
   (gen-func (get-map-data [nil map-type] [kml map-path] uri-base))])

(defn get-map-routes
  [& {:keys [gen-data uri-base]}]
  (->> uri-base
       (vector)
       (cartesian-product gen-data (get-map-files))
       (map #(apply get-map-route %))
       (into {})))
