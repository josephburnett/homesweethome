(ns homesweethome.data.entity
  (:require [homesweethome.config :refer [config entity-path]]
            [homesweethome.data.hsh :refer [read-hsh]]
            [me.raynes.fs :refer [find-files split mkdirs exists?]]
            [clojure.string :refer [join]])
  (:import [java.io File]))

(defn scan [type]
  (let [files (find-files (entity-path type) #".*\.hsh$")]
    (filter (complement nil?)
            (map #(try
                    (read-hsh (.getAbsolutePath %))
                    (catch Exception e nil))
                 files))))

(defn search [type f]
  (let [type-string (.substring (str type) 1)]
    (filter #(and (= type-string (:type %))
                  (f %))
            (scan type))))

(defn entity-init []
  (doall
    (map #(let [path (join File/separator 
                           (concat (split (entity-path (first %))) '(".homesweethome")))]
            (if (not (exists? path))
              (mkdirs path)))
         (seq (get-in config [:entities])))))
