(ns homesweethome.image.cache
  (:require [homesweethome.image.manip :as img]
            [homesweethome.config :refer [cache-path entity-path]]
            [me.raynes.fs :refer [exists?]]
            [clojure.java.io :refer [as-file]]))

(defn thumbnail [entity]
  (let [md5 (:md5 entity)
        _ (assert (not (nil? md5)))
        t-file (str (cache-path) "/" (:md5 entity) "_thumb.jpg")
        filename (str (entity-path (keyword (:type entity))) "/" (:key entity))
        _ (println "filename: " filename)]
    (if (exists? t-file)
      (as-file t-file)
      (let [image (img/thumbnail (first (img/pdf-to-images filename)))]
        (img/save-image image t-file)
        (as-file t-file)))))

(defn image [entity]
  (let [md5 (:md5 entity)
        i-file (str (cache-path) "/" (:md5 entity) "_image.jpg")
        filename (str (entity-path (keyword (:type entity))) "/" (:key entity))]
    (if (exists? i-file)
      (as-file i-file)
      (let [entity-image (img/stack (img/pdf-to-images filename))]
        (img/save-image entity-image i-file)
        (as-file i-file)))))
