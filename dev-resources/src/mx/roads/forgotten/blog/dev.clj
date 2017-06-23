(ns mx.roads.forgotten.blog.dev
  "FRMX Blog development namespace

  This namespace is particularly useful when doing active development on the
  FRMX Blog application."
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint print-table]]
    [clojure.reflect :refer [reflect]]
    [clojure.string :as string]
    [clojure.tools.namespace.repl :as repl]
    [clojure.walk :refer [macroexpand-all]]
    [clojusc.twig :as logger]
    [dragon.blog :as blog]
    [dragon.config :as config]
    [dragon.generator :as gen]
    [dragon.util :as util]
    [dragon.web :as web]
    [mx.roads.forgotten.blog.cli :as cli]
    [mx.roads.forgotten.blog.main :as main]
    [mx.roads.forgotten.blog.reader :as reader]
    [mx.roads.forgotten.blog.routes :as routes]
    [mx.roads.forgotten.blog.web.content.data :as data]
    [mx.roads.forgotten.blog.web.content.page :as page]
    [selmer.parser :as selmer]
    [taoensso.timbre :as log]
    [trifl.java :refer [show-methods]]))

(logger/set-level! ['mx.roads.forgotten.blog] :debug)

;;; Aliases

(def reload #'repl/refresh)
(def reset #'repl/refresh)
