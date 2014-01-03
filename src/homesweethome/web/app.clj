(ns homesweethome.web.app
  (:require [homesweethome.config :refer [config]]
            [homesweethome.data.entities.pdf :as pdf-entity]
            [homesweethome.web.page.pdf :as pdf-view]
            [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY]]
            [hiccup.page :refer [html5]]))

(defresource view-pdf
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok 
  (fn [ctx] (let [key (get-in ctx [:request :params "key"])]
              (println "key:" key)
              (html5 (map #(pdf-view/render %) 
                          (pdf-entity/load-by-key key))))))

(defresource browse-pdf
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok 
  (fn [ctx] (let [key-prefix (get-in ctx [:request :params "key-prefix"])]
              (println "key-prefix:" key-prefix)
              (html5 (pdf-view/render-ul 
                       (pdf-entity/load-by-key-prefix key-prefix))))))

(defroutes app
  (ANY "/pdf/view" [] (wrap-params view-pdf))
  (ANY "/pdf/browse" [] (wrap-params browse-pdf)))
