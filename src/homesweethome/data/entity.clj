(ns homesweethome.data.entity
  (:require [homesweethome.config :refer [config entity-path]]
            [homesweethome.data.hsh :refer [read-hsh write-hsh]]
            [me.raynes.fs :refer [file base-name exists? directory? iterate-dir 
                                  find-files split mkdirs copy+]]
            [clojure.string :refer [join]]
            [clojure.java.io :refer [delete-file]])
  (:import [java.io File]))

(def hsh-folder "/.homesweethome")

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

(defn categories [type]
  (let [path (entity-path type)]
    (sort (map #(.substring % 1)
               (filter #(and (not (.startsWith % hsh-folder))
                             (not (.isEmpty %)))
                       (map (comp
                              #(.substring % (count path))
                              #(.getAbsolutePath %) 
                              first)
                            (iterate-dir (entity-path type))))))))

(defn categorize [key type category]
  (let [ep (entity-path type)
        sep File/separator
        from (str ep sep key)
        from-hsh (str ep sep ".homesweethome" sep key ".hsh")
        to (str ep sep category sep (base-name key))
        to-hsh (str ep sep ".homesweethome" sep category sep (base-name key) ".hsh")
        new-key (.substring to (+ 1 (count ep)))]
    (if (not (and (exists? from)
                  (exists? from-hsh)
                  (not (exists? to))
                  (not (exists? to-hsh))
                  (some #(= category %) (categories type))))
      (throw (Exception. (str "Cannot move " from " to " to " and " from-hsh " to " to-hsh)))
      (do
        (copy+ from to)
        (copy+ from-hsh to-hsh)
        (write-hsh to-hsh (assoc (read-hsh to-hsh) :key new-key))
        (delete-file (file from))
        (delete-file (file from-hsh))))))
      