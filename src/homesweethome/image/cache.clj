(ns homesweethome.image.cache
  (:require [homesweethome.image.manip :as img]
            [homesweethome.config :refer [cache-path entity-path]]
            [me.raynes.fs :refer [exists?]]))

(defn thumbnail [entities]
  (let [entity (first entities)
        md5 (:md5 entity)
        _ (assert (not (nil? md5)))
        t-file (str (cache-path) "/" (:md5 entity) "_thumb.jpg")
        filename (str (entity-path (keyword (:type entity))) "/" (:key entity))
        _ (println "filename: " filename)]
    (if (exists? t-file)
      (img/load-image t-file)
      (let [image (img/thumbnail (first (img/pdf-to-images filename)))]
        (img/save-image image t-file)
        image))))
