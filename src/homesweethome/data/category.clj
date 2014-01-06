(ns homesweethome.data.category
  (:require [homesweethome.config :refer [entity-path]]
            [me.raynes.fs :refer [base-name exists? directory? iterate-dir]])
  (:import [java.io File]))

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

(defn categorize [key type category]
  (let [ep (entity-path type)
        sep File/separator
        from (str ep sep key)
        from-hsh (str ep sep ".homesweethome" sep key)
        to (str ep sep category sep (base-name key))
        to-hsh (str ep sep ".homesweethome" sep category sep (base-name key))]
    (if (and (exists? from)
             (exists? from-hsh)
             (not (exists? to))
             (not (exists? to-hsh))
             (contains? (categories type) category))
      (println "I'm going to move" from "to" to "and" from-hsh "to" to-hsh)
      (println "Cannot move" from "to" to "and" from-hsh "to" to-hsh))))
