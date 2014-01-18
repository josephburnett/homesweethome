(ns homesweethome.web.app
  (:require [homesweethome.config :refer [config cache-path]]
            [homesweethome.data.entities.pdf :as pdf-entity]
            [homesweethome.web.page.pdf :as pdf-view]
            [homesweethome.data.entity :refer [key-category]]
            [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY]]
            [compojure.route :refer [resources]]
            [homesweethome.image.cache :as i-cache])
  (:import [java.io ByteArrayInputStream]))

(defresource view-pdf
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok 
  (fn [ctx] (let [key (get-in ctx [:request :params "key"])]
              (pdf-view/render ctx
                (pdf-entity/load-by-key key)))))

(defresource browse-pdf
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok 
  (fn [ctx] (let [key-prefix (get-in ctx [:request :params "key-prefix"])]
              (pdf-view/render-preview ctx
                (pdf-entity/load-by-key-prefix key-prefix)))))

(defresource categorize-pdf
  :allowed-methods [:post]
  :available-media-types ["text/html"]
  :handle-created
  (fn [ctx] (let [key (get-in ctx [:request :params "key"])
                  category (get-in ctx [:request :params "key-prefix"])]
              (pdf-view/render-preview ctx
                (do
                  (pdf-entity/categorize key category)
                  (pdf-entity/load-by-key-prefix (key-category key)))))))

(defresource thumbnail-pdf
  :allowed-methods [:get]
  :available-media-types ["image/jpeg"]
  :handle-ok
  (fn [ctx] (let [key (get-in ctx [:request :params "key"])
                  pdf (first (pdf-entity/load-by-key key))]
              (i-cache/thumbnail pdf))))

(defresource image-pdf
  :allowed-methods [:get]
  :available-media-types ["image/jpeg"]
  :handle-ok
  (fn [ctx] (let [key (get-in ctx [:request :params "key"])
                  pdf (first (pdf-entity/load-by-key key))]
              (i-cache/image pdf))))

(defroutes app
  (ANY "/pdf/view" [] (wrap-params view-pdf))
  (ANY "/pdf/browse" [] (wrap-params browse-pdf))
  (ANY "/pdf/categorize" [] (wrap-params categorize-pdf))
  (ANY "/pdf/thumbnail" [] (wrap-params thumbnail-pdf))
  (ANY "/pdf/image" [] (wrap-params image-pdf))
  (resources "/"))
