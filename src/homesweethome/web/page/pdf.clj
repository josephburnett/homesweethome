(ns homesweethome.web.page.pdf
  (:require [homesweethome.config :refer [entity-path]]
            [hiccup.core :refer [html]]
            [me.raynes.fs :refer [absolute-path expand-home]]))

(def pdf-store (entity-path :pdf))
(def text-preview-length 500)

(defn clean-text [text]
  (apply str (take text-preview-length (re-seq #"[\d\w\s\.\-]" text))))

(defn render [pdf]
  (html
    [:div {:id (:key pdf) :class "pdf"}
     [:span {:class "pdf-key"} (:key pdf)]
     [:span {:class "pdf-folder"} (expand-home (str pdf-store (:file pdf)))]]))
     ; TODO build pdf-folder in a generic way
     
(defn render-li [pdf]
  (html
    [:div {:id (:key pdf) :class "pdf-li"}
     [:span {:class "pdf-li-key"} (:key pdf)]
     [:span {:class "pdf-li-text"} (clean-text (:text pdf))]]))

(defn render-ul [pdf-list]
  (html
    [:div {:class "pdf-ul"}
     [:ul (map #(render-li %) pdf-list)]]))
