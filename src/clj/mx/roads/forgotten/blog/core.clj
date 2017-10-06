(ns mx.roads.forgotten.blog.core
  (:require [clojusc.twig :as logger]
            [dragon.blog.core :as blog]
            [dragon.blog.generator :as gen]
            [dragon.config :as config]
            [dragon.util :as util]
            [mx.roads.forgotten.blog.email.content :as email-content]
            [mx.roads.forgotten.blog.routes :refer [gen-routes routes]]
            [mx.roads.forgotten.blog.social.content :as social-content]
            [trifl.core :refer [sys-prop]]
            [trifl.docs :as docs]))

(defn version
  []
  (let [version (sys-prop "blog.version")
        build (util/get-build)]
    (format "FRMX Blog version %s, build %s\n" version build)))

(defn generate
  [system]
  (let [posts (blog/process system)
        generated-routes (gen-routes system posts)]
    (gen/run system generated-routes)
    (email-content/gen system posts)
    (social-content/gen system posts)))
