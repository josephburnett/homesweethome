(ns homesweethome.data.entity
  (:require [homesweethome.config :refer [config]]
            [homesweethome.data.hsh :refer [read-hsh]]
            [me.raynes.fs :refer [find-files expand-home]]))

(defn scan []
  (let [entities (get config "entities")
        entity-paths (map #(get (second %) "path") (seq entities))
        files (flatten (map #(find-files (expand-home %) #".*\.hsh$") entity-paths))]
    (filter (complement nil?)
            (map #(try
                    (read-hsh (.getAbsolutePath %))
                    (catch Exception e nil))
                 files))))

(defn search [type f]
  (filter #(and (= type (get % "type"))
                (f %))
          (scan)))
