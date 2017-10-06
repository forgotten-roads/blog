(ns mx.roads.forgotten.blog.components.system
  (:require [com.stuartsierra.component :as component]
            [dragon.components.config :as config]
            [dragon.components.event :as event]
            [dragon.components.httpd :as httpd]
            [dragon.components.logging :as logging]
            [dragon.components.system :as system]))

(defn initialize [config-builder]
  (component/system-map
   :config (config/create-config-component config-builder)
   :logging (component/using
             (logging/create-logging-component)
             [:config])
   :event (component/using
           (event/create-event-component)
           [:config :logging])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Managment Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn stop
  ([system]
   (component/stop system))
  ([system component-key]
   (->> system
        (component-key)
        (component/stop)
        (assoc system component-key))))

(defn start
  ([]
   (start (initialize)))
  ([system]
   (component/start system))
  ([system component-key]
   (->> system
        (component-key)
        (component/start)
        (assoc system component-key))))

(defn restart
  ([system]
   (-> system
       (stop)
       (start)))
  ([system component-key]
   (-> system
       (stop component-key)
       (start component-key))))
