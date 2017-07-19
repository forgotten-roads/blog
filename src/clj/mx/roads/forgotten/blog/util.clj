(ns mx.roads.forgotten.blog.util)

(defn zip
  [& colls]
  (partition (count colls)
             (apply interleave colls)))
