(ns homesweethome.data.category
  (:require [homesweethome.config :refer [entity-path]]
            [me.raynes.fs :refer [directory? iterate-dir]]))

(def hsh-folder "/.homesweethome")

(defn categories [type]
  (let [path (entity-path type)]
    ;; use fs to list directories
    (sort (map #(.substring % (+ 1 (count hsh-folder)))
               (filter #(and (.startsWith % hsh-folder)
                             (not (= % hsh-folder)))
                       (map (comp
                              #(.substring % (count path))
                              #(.getAbsolutePath %) 
                              first)
                            (iterate-dir (entity-path type))))))))
