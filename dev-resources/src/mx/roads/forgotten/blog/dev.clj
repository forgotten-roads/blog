(ns mx.roads.forgotten.blog.dev
  "FRMX Blog development namespace

  This namespace is particularly useful when doing active development on the
  FRMX Blog application."
  (:require
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
    [dragon.blog.generator :as generator]
    [dragon.blog.post :as post]
    [dragon.cli.core :as dragon-cli]
    [dragon.components.core :as component-api]
    [dragon.components.system :as components]
    [dragon.config.core :as config]
    [dragon.data.sources.core :as data-source]
    [dragon.data.sources.impl.redis :as redis-db]
    [dragon.main :as dragon-main]
    [dragon.util :as dragon-util]
    [dragon.web.core :as web]
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

(logger/set-level! ['mx.roads.forgotten.blog 'dragon] :info)

(defn show-lines-with-error
  "Process posts and show the lines of text that threw exceptions."
  []
  (->> (blog/get-posts)
       (map #(->> %
                 (post/add-post-data)
                 :text))
       (pprint)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   State & Transition Vars   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def state :stopped)
(def system nil)
(def valid-stop-transitions #{:started :running})
(def invalid-init-transitions #{:initialized :started :running})
(def invalid-deinit-transitions #{:started :running})
(def invalid-start-transitions #{:started :running})
(def invalid-stop-transitions #{:stopped})
(def invalid-run-transitions #{:running})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Initial Setup & Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(logger/set-level! '[dragon clojang] :debug)

(defn redis
  [& args]
  (apply redis-db/cmd (concat [system] args)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   State Management   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn init
  ([]
    (init :default))
  ([mode]
    (if (contains? invalid-init-transitions state)
      (log/error "System has aready been initialized.")
      (do
        (alter-var-root #'system
          (constantly (system/init)))
        (alter-var-root #'state (fn [_] :initialized))))
    state))

(defn deinit
  []
  (if (contains? invalid-deinit-transitions state)
    (log/error "System is not stopped; please stop before deinitializing.")
    (do
      (alter-var-root #'system (fn [_] nil))
      (alter-var-root #'state (fn [_] :uninitialized))))
  state)

(defn start
  ([]
    (start :default))
  ([mode]
    (when (nil? system)
      (init mode))
    (if (contains? invalid-start-transitions state)
      (log/error "System has already been started.")
      (do
        (alter-var-root #'system component/start)
        (alter-var-root #'state (fn [_] :started))))
    state))

(defn stop
  []
  (if (contains? invalid-stop-transitions state)
    (log/error "System already stopped.")
    (do
      (alter-var-root #'system
        (fn [s] (when s (component/stop s))))
      (alter-var-root #'state (fn [_] :stopped))))
  state)

(defn restart
  []
  (stop)
  (start))

(defn run
  []
  (if (contains? invalid-run-transitions state)
    (log/error "System is already running.")
    (do
      (if (not (contains? invalid-init-transitions state))
        (init))
      (if (not (contains? invalid-start-transitions state))
        (start))
      (alter-var-root #'state (fn [_] :running))))
  state)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Reloading Management   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn -refresh
  ([]
    (repl/refresh))
  ([& args]
    (apply #'repl/refresh args)))

(defn refresh
  "This is essentially an alias for clojure.tools.namespace.repl/refresh."
  [& args]
  (if (contains? valid-stop-transitions state)
    (stop))
  (apply -refresh args))

(defn reset
  []
  (stop)
  (deinit)
  (refresh :after 'dragon.dev/run))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Aliases   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def reload #'reset)
