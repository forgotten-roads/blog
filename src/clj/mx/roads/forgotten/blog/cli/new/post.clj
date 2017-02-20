(ns mx.roads.forgotten.blog.cli.new.post
  (:require [clojure.pprint :refer [pprint]]
            [mx.roads.forgotten.blog.stub :as stub]
            [mx.roads.forgotten.blog.util :as util]
            [taoensso.timbre :as log]))

(defn help-cmd
  [& args]
  (util/print-docstring 'mx.roads.forgotten.blog.cli.new.post 'run))

(defn run
  "
  Usage:
  ```
    frmx new post SUBCOMMAND [help]
  ```

  A SUBCOMMAND is required.

  Subcommands:
  ```
    md         Create a new post stub in Markdown format
    clj        Create a new post stub in Clojure format
    edn        Create a new post stub in EDN format
    html       Create a new post stub in HTML format
    rfc5322    Create a new post stub in email message format; in this
                 case, no metadata file is created: message headers
                 are used instead
  ```"
  [[cmd & args]]
  (log/debug "Got cmd:" cmd)
  (log/debug "Got args:" args)
  (let [passed-date (first args)
        date (or passed-date (util/now))]
    (case cmd
      :md (stub/make-markdown-post date)
      :clj (stub/make-clojure-post date)
      :edn (stub/make-edn-post date)
      :html (stub/make-html-post date)
      :rfc5322 (stub/make-rfc5322-post date)
      :rfc (stub/make-rfc5322-post date)
      :5322 (stub/make-rfc5322-post date)
      (help-cmd))))
