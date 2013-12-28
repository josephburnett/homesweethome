(ns homesweethome.web.page.pdf
  (:require [homesweethome.config :refer [entity-path]]
            [hiccup.core :refer [html]]
            [me.raynes.fs :refer [absolute-path expand-home]]))

(def pdf-store (entity-path :pdf))

(defn render [pdf]
  (html
    [:div {:id (:id pdf) :class "pdf"}
     [:span {:class "pdf-id"} (:id pdf)]
     [:span {:class "pdf-folder"} (expand-home (str pdf-store (:file pdf)))]]))
     ; TODO build pdf-folder in a generic way
     