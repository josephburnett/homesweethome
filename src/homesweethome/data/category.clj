(ns homesweethome.data.category
  (:require [homesweethome.config :refer [entity-path]]
            [me.raynes.fs :refer [directory? iterate-dir]]))

(defn categories [type]
  (let [path (entity-path type)]
    ;; use fs to list directories
    (map #(.substring % 1)
         (filter #(and (not (.isEmpty %))
                       (not (.startsWith % "/.homesweethome")))
                 (map (comp
                        #(.substring % (count path))
                        #(.getAbsolutePath %) 
                        first)
                      (iterate-dir (entity-path :pdf)))))))
