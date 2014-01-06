(ns homesweethome.data.entities.pdf
  (:require [me.raynes.fs :refer [absolute-path split exec expand-home]]
            [homesweethome.data.entity :as entity]
            [homesweethome.data.hsh :refer [write-hsh]]
            [homesweethome.config :refer [entity-path]]
            [digest :refer [md5]]
            [clojure.java.io :refer [as-file]]
            [clojure.string :refer [join]])
  (:import [java.io File]))

(defn readpdf [filename]
  "sample text")
;  (let [pdftext (exec "resources/bin/readpdf" filename)]
;    (if (= 0 (:exit pdftext))
;      (:out pdftext)
;      (throw (Exception. (str "Non zero readpdf exit code: " (:exit pdftext)))))))

(defn intake [filename]
  (if (.endsWith filename ".pdf")
    ; TODO use extension function
    (let [text (readpdf filename)
          key (join File/separator 
                    (drop (count (split (entity-path :pdf)))
                          (split (absolute-path filename))))
          hsh-filename (join File/separator
                             [(entity-path :pdf)
                                   ".homesweethome"
                                   (str key ".hsh")])]
      (write-hsh hsh-filename 
                 {:type "pdf"
                  :text text
                  :md5 (md5 (as-file filename))
                  :key key
                  :tags []}))))

(defn load-by-key [key]
  (entity/search :pdf #(= key (:key %))))

(defn load-by-md5 [md5]
  (entity/search :pdf #(= md5 (:md5 %))))

(defn load-by-key-prefix [key-prefix]
  (entity/search :pdf #(.startsWith (:key %) key-prefix)))

(defn categorize [key category]
  (entity/categorize key :pdf category))
