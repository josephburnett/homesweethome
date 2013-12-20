(ns homesweethome.web.app
  (:require [homesweethome.config :refer [config]]
            [liberator.core :refer [resource defresource]]
            [compojure.core :refer [defroutes ANY]]))

(defresource view-entity [id]
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok (fn [_] (println "handle-ok: " id)))

(defroutes app
  (ANY "/view/entity/:id" [id] (view-entity id)))
