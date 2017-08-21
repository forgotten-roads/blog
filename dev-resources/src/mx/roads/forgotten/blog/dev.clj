(ns mx.roads.forgotten.blog.dev
  "FRMX Blog development namespace

  This namespace is particularly useful when doing active development on the
  FRMX Blog application."
  (:require
    [clojure.math.combinatorics :refer [cartesian-product]]
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint print-table]]
    [clojure.reflect :refer [reflect]]
    [clojure.string :as string]
    [clojure.tools.namespace.repl :as repl]
    [clojure.walk :refer [macroexpand-all]]
    [clojusc.twig :as logger]
    [dragon.blog :as blog]
    [dragon.blog.post :as post]
    [dragon.config :as config]
    [dragon.generator :as gen]
    [dragon.util :as util]
    [dragon.web :as web]
    [mx.roads.forgotten.blog.cli :as cli]
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
    [taoensso.timbre :as log]
    [trifl.fs :as fs]
    [trifl.java :refer [show-methods]]))

(logger/set-level! ['mx.roads.forgotten.blog 'dragon] :info)

;;; Aliases

(def reload #'repl/refresh)
(def reset #'repl/refresh)

(defn show-lines-with-error
  "Process posts and show the lines of text that threw exceptions."
  []
  (->> (blog/get-posts)
       (map #(->> %
                 (post/add-post-data)
                 :text))
       (pprint)))
