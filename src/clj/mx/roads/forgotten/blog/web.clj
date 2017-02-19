(ns mx.roads.forgotten.blog.web
  (:require [mx.roads.forgotten.blog.config :as config]
            [mx.roads.forgotten.blog.web.content.page :as page]
            [stasis.core :as stasis]))

(def routes
  {"/index.html" (page/front-page {})})

(def app (stasis/serve-pages routes))

(defn run
  [& args]
  :todo)
