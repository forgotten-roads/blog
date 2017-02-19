(ns mx.roads.forgotten.blog.web.content.data)

(defn base
  [req]
  (merge
    req
    {:index "index"
     :about "about"
     :credits "credits"}))

(defn index
  [req]
  (merge
    req
    (base req)
    {:active "index"}))

(defn about
  [req]
  (merge
    req
    (base req)
    {:active "about"
     :subtitle " :: About"}))

(defn credits
  [req]
  (merge
    req
    (base req)
    {:active "credits"
     :subtitle " :: Credits"}))
