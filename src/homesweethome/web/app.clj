(ns homesweethome.web.app
  (:require [homesweethome.config :refer [config]]
            [homesweethome.data.entities.pdf :as pdf-entity]
            [homesweethome.web.page.pdf :as pdf-view]
            [liberator.core :refer [resource defresource]]
            [compojure.core :refer [defroutes ANY]]
            [hiccup.page :refer [html5]]))

(defresource view-pdf [key]
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok (fn [_] (html5 (map #(pdf-view/render %) 
                                 (pdf-entity/load-by-key key)))))

(defresource browse-pdf [key-prefix]
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok (fn [_] (html5 (pdf-view/render-ul 
                              (pdf-entity/load-by-key-prefix key-prefix)))))

(defroutes app
  (ANY "/pdf/view/:key" [key] (view-pdf key))
  (ANY "/pdf/browse/:key-prefix" [key-prefix] (browse-pdf key-prefix)))
