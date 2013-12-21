(ns homesweethome.web.page.pdf
  (:require [homesweethome.config :refer [config]]
            [hiccup.core :refer [html]]
            [me.raynes.fs :refer [absolute-path expand-home]]))

(def pdf-store (get (get (get config "entities") "pdf") "path"))
; this is ridiculous ... i should convert the config from json -> edn with keywords

(defn render [pdf]
  (html
    [:div {:id (get pdf "id") :class "pdf"}
     [:span {:class "pdf-id"} (get pdf "id")]
     [:span {:class "pdf-folder"} (expand-home (str pdf-store (get pdf "file")))]]))
