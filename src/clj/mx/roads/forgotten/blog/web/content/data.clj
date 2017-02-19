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
    {:active "about"}))

(defn credits
  [req]
  (merge
    (base req)
    {:active "credits"}))

(defn starter
  [req]
  (merge
    (base req)
    {}))

(defn post
  [req]
  (merge
    (base req)
    {}))
