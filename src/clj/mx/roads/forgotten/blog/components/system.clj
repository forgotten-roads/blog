(ns mx.roads.forgotten.blog.components.system
  (:require
    [com.stuartsierra.component :as component]
    [dragon.components.config :as config]
    [dragon.components.event :as event]
    [dragon.components.httpd :as httpd]
    [dragon.components.logging :as logging]
    [dragon.components.system :as system]
    [dragon.config.core :refer [build]
                        :rename {build build-config}]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Managment Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def stop #'component/stop)

(defn init
  []
  (system/initialize-with-web build-config))

(defn start
  [init-data]
  (system/start init-data))

(def restart #'system/restart)
