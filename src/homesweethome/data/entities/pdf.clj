(ns homesweethome.data.entities.pdf
  (:require [me.raynes.fs :refer [exec expand-home]]
            [homesweethome.data.entity :refer [search]]
            [homesweethome.data.hsh :refer [write-hsh]]
            [homesweethome.config :refer [config]]
            [digest :refer [md5]]
            [clojure.java.io :refer [as-file]]))

(defn hsh-id [filename]
  (md5 (as-file filename)))

(defn readpdf [filename]
  "sample text")
;  (let [pdftext (exec "resources/bin/readpdf" filename)]
;    (if (= 0 (:exit pdftext))
;      (:out pdftext)
;      (throw (Exception. (str "Non zero readpdf exit code: " (:exit pdftext)))))))

(defn intake [filename]
  (if (.endsWith filename ".pdf")
    (let [text (readpdf filename)
          name (str (expand-home (get-in config [:entities :pdf :path])) 
                    "/" 
                    (hsh-id filename))]
      (write-hsh name {:type "pdf"
                       :text text
                       :file filename
                       :tags []}))))

(defn load [id]
  (search "pdf" #(= id (:id %))))
