(ns homesweethome.web.page.pdf
  (:require [homesweethome.config :refer [entity-path]]
            [homesweethome.data.entity :refer [categories]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5]]
            [hiccup.form :refer [drop-down with-group submit-button form-to]]
            [hiccup.element :refer [link-to]]
            [me.raynes.fs :refer [absolute-path expand-home]]
            [clojure.string :refer [split join]]))

(def pdf-store (entity-path :pdf))
(def text-preview-length 500)

(defn clean-text [text]
  (apply str (take text-preview-length (re-seq #"[\d\w\s\.\-]" text))))

(defn category-selector [default]
  (form-to [:get "/pdf/browse"]
    (drop-down "key-prefix" (categories :pdf) default)
    (submit-button "browse")))

(defn menu [ctx]
  (let [default-category (get-in ctx [:request :params "key-prefix"])]
    (html
      (category-selector default-category))))

(defn prefix [key]
  (join "/" (drop-last (split key #"\/"))))

(defn pdf-link [pdf]
  (str "/pdf/view?key=" (:key pdf)
       "&key-prefix=" (prefix (:key pdf))))

(defn render [ctx pdfs]
  (html5
    (menu ctx)
    (map #(html
            (link-to 
              (pdf-link %)
              [:div {:id (:key %) :class "pdf"}
               [:span {:class "pdf-key"} (:key %)]
               [:span {:class "pdf-folder"} (expand-home (str pdf-store (:file %)))]]))
         pdfs)))
     
(defn render-preview [ctx pdfs]
  (html5
    (menu ctx)
    (map #(html
            (link-to
              (pdf-link %)
              [:div {:id (:key %) :class "pdf-li"}
               [:span {:class "pdf-preview-key"} (:key %)]
               [:span {:class "pdf-preview-text"} (clean-text (:text %))]]))
         pdfs)))
