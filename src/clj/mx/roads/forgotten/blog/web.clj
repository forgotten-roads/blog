(ns mx.roads.forgotten.blog.web
  (:require [mx.roads.forgotten.blog.config :as config]
            [mx.roads.forgotten.blog.web.content.page :as page]
            [org.httpkit.server :as server]
            [stasis.core :as stasis]
            [taoensso.timbre :as log]))

(def routes
  {"/index.html" (page/front-page)
   "/about.html" (page/about)})

(def app (stasis/serve-pages routes))

(defn run
  [& args]
  (let [port (config/get-port)]
    (log/infof (str "Starting development HTTP server on port %s "
                    "using dynamic content ...")
               port)
    (server/run-server app {:port port})))
