(ns mx.roads.forgotten.blog.dev
  "FRMX Blog development namespace

  This namespace is particularly useful when doing active development on the
  FRMX Blog application."
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint print-table]]
            [clojure.reflect :refer [reflect]]
            [clojure.string :as string]
            [clojure.tools.namespace.repl :as repl]
            [clojure.walk :refer [macroexpand-all]]
            [clojusc.twig :as logger]
            [mx.roads.forgotten.blog.cli :as cli]
            [mx.roads.forgotten.blog.generator :as generator]
            [mx.roads.forgotten.blog.main :as main]
            [mx.roads.forgotten.blog.util :as util]
            [mx.roads.forgotten.blog.web :as web]
            [mx.roads.forgotten.blog.web.content.data :as data]
            [mx.roads.forgotten.blog.web.content.page :as page]
            [selmer.parser :as selmer]
            [taoensso.timbre :as log]))

(logger/set-level! ['mx.roads.forgotten.blog] :debug)

(defn show-methods
  ""
  [obj]
  (print-table
    (sort-by :name
      (filter (fn [x]
                (contains? (:flags x) :public))
              (:members (reflect obj))))))

;;; Aliases

(def reload #'repl/refresh)
(def reset #'repl/refresh)
