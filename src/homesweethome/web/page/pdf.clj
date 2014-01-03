(ns homesweethome.web.page.pdf
  (:require [homesweethome.config :refer [entity-path]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5]]
            [me.raynes.fs :refer [absolute-path expand-home]]))

(def pdf-store (entity-path :pdf))
(def text-preview-length 500)

(defn clean-text [text]
  (apply str (take text-preview-length (re-seq #"[\d\w\s\.\-]" text))))

(defn render [pdfs]
  (html5
    (map #(html
            [:div {:id (:key %) :class "pdf"}
             [:span {:class "pdf-key"} (:key %)]
             [:span {:class "pdf-folder"} (expand-home (str pdf-store (:file %)))]])
         pdfs)))
     
(defn render-preview [pdfs]
  (html5
    (map #(html
           [:div {:id (:key %) :class "pdf-li"}
            [:span {:class "pdf-preview-key"} (:key %)]
            [:span {:class "pdf-preview-text"} (clean-text (:text %))]])
         pdfs)))
