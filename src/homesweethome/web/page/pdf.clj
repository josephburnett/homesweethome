(ns homesweethome.web.page.pdf
  (:require [homesweethome.config :refer [entity-path]]
            [homesweethome.data.entity :refer [categories key-category]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-css]]
            [hiccup.form :refer [drop-down with-group submit-button form-to hidden-field]]
            [hiccup.element :refer [link-to image]]
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

(defn pdf-link [pdf]
  (str "/pdf/view?key=" (:key pdf)
       "&key-prefix=" (key-category (:key pdf))))

(defn pdf-image-link [pdf]
  (str "/pdf/image?key=" (:key pdf)))

(defn pdf-thumbnail-link [pdf]
  (str "/pdf/thumbnail?key=" (:key pdf)))

(defn categorize-link [pdf]
  (let [curr-cat (key-category (:key pdf))]
    (form-to [:post "/pdf/categorize"]
      (drop-down "key-prefix" (filter #(not (= curr-cat %)) (categories :pdf)))
      (hidden-field "key" (:key pdf))
      (submit-button "categorize"))))

(defn render [ctx pdfs]
  (html5
    (include-css "/css/pdf.css")
    (menu ctx)
    (map #(html
            [:div {:id (:key %) :class "pdf"}
             [:span {:class "pdf-key"} (:key %)]
             [:span {:class "pdf-folder"} (expand-home (str pdf-store (:file %)))]
             [:span {:class "pdf-categorize-link"}
              (categorize-link %)]
             [:span {:class "pdf-image"}
              (image (pdf-image-link %))]])
         pdfs)))
     
(defn render-preview [ctx pdfs]
  (html5
    (include-css "/css/pdf.css")
    (menu ctx)
    (map #(html
            [:div {:id (:key %) :class "pdf-preview"}
             [:span {:class "pdf-view-link"}
              (link-to
                (pdf-link %)
                [:span {:class "pdf-preview-key"} (:key %)]
                [:span {:class "pdf-preview-text"} (clean-text (:text %))])]
              [:span {:class "pdf-categorize-link"}
               (categorize-link %)]
              [:span {:class "pdf-thumbnail"}
               (image (pdf-thumbnail-link %))]])
         pdfs)))
