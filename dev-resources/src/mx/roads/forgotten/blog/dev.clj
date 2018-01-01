(ns mx.roads.forgotten.blog.dev
  "FRMX Blog development namespace

  This namespace is particularly useful when doing active development on the
  FRMX Blog application."
  (:require
    [cheshire.core :as json]
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.java.shell :as shell]
    [clojure.math.combinatorics :refer [cartesian-product]]
    [clojure.pprint :refer [pprint print-table]]
    [clojure.reflect :refer [reflect]]
    [clojure.string :as string]
    [clojure.tools.namespace.repl :as repl]
    [clojure.walk :refer [macroexpand-all]]
    [clojusc.twig :as logger]
    [com.stuartsierra.component :as component]
    [datomic.client :as datomic]
    [dragon.blog.content.block :as block]
    [dragon.blog.content.rfc5322 :as rfc5322]
    [dragon.blog.core :as blog]
    [dragon.blog.generator :as gen]
    [dragon.blog.post.core :as post]
    [dragon.cli.core :as dragon-cli]
    [dragon.components.core :as component-api]
    [dragon.components.system :as components]
    [dragon.config.core :as config]
    [dragon.data.sources.core :as data-source]
    [dragon.data.sources.impl.redis :as redis-db]
    [dragon.dev.system :as dev-system]
    [dragon.main :as dragon-main]
    [dragon.selmer.tags.flickr :as flickr]
    [dragon.util :as dragon-util]
    [ltest.core :as ltest]
    [markdown.core :as md]
    [mx.roads.forgotten.blog.cli.core :as cli]
    [mx.roads.forgotten.blog.components.system :as system]
    [mx.roads.forgotten.blog.core :as core]
    [mx.roads.forgotten.blog.email.content :as email-content]
    [mx.roads.forgotten.blog.email.delivery :as email-delivery]
    [mx.roads.forgotten.blog.main :as main]
    [mx.roads.forgotten.blog.maps :as maps]
    [mx.roads.forgotten.blog.reader :as reader]
    [mx.roads.forgotten.blog.routes :as routes]
    [mx.roads.forgotten.blog.social.content :as social-content]
    [mx.roads.forgotten.blog.social.google-plus :as gplus]
    [mx.roads.forgotten.blog.social.twitter :as twitter]
    [mx.roads.forgotten.blog.util :as mx-util]
    [mx.roads.forgotten.blog.web.content.data :as data]
    [mx.roads.forgotten.blog.web.content.page :as page]
    [selmer.parser :as selmer]
    [taoensso.carmine :as car :refer [wcar]]
    [taoensso.timbre :as log]
    [trifl.core :refer [->int]]
    [trifl.fs :as fs]
    [trifl.java :refer [show-methods]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Initial Setup & Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(logger/set-level! ['mx.roads.forgotten.blog 'dragon] :info)

(dev-system/set-generator-ns "mx.roads.forgotten.blog.core")
(dev-system/set-system-ns "mx.roads.forgotten.blog.components.system")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   State Management   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def startup #'dev-system/startup)
(def shutdown #'dev-system/shutdown)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Reloading Management   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn reset
  []
  (dev-system/shutdown)
  (repl/refresh :after 'dragon.dev.system/startup))

(def refresh #'repl/refresh)
(def refresh #'reset)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def redis #'dev-system/redis)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def show-lines-with-error #'dev-system/show-lines-with-error)
(def show-posts #'dev-system/show-posts)
(def generate #'dev-system/generate)
(def force-regenerate #'dev-system/force-regenerate)
