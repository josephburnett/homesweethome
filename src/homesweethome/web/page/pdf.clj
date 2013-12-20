(ns homesweethome.web.page.pdf
  (:require [hiccup.core :refer [html]]))

(defn render [pdf]
  (html
    [:div {:id (get pdf "id") :class "pdf"}
     [:span {:class "pdf-id"} (get pdf "id")]]))
