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
    (base req)
    {:active "index"}))

(defn about
  [req]
  (merge
    (base req)
    {:active "about"
     :subtitle " :: About"}))

(defn credits
  [req]
  (merge
    (base req)
    {:active "credits"
     :subtitle " :: Credits"}))

(defn starter
  [req]
  (merge
    (base req)
    {:subtitle " :: Starter Page"}))

(defn post
  [req]
  (merge
    (base req)
    {:subtitle " :: Blog Post"}))
