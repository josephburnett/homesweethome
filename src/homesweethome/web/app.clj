(ns homesweethome.web.app
  (:require [homesweethome.config :refer [config]]
            [homesweethome.data.entities.pdf :as pdf]
            [liberator.core :refer [resource defresource]]
            [compojure.core :refer [defroutes ANY]]))

(defresource view-pdf [id]
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok (fn [_] (pdf/view id)))

(defroutes app
  (ANY "/pdf/view/:id" [id] (view-pdf id)))
