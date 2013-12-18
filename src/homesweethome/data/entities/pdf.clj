(ns homesweethome.data.entities.pdf
  (:require [me.raynes.fs :refer [exec]]))

(defn intake [filename]
  (println "Intaking PDF: " filename))

(defn readpdf [filename]
  (let [pdftext (exec "resources/bin/readpdf" filename)]
    (if (= 0 (:exit pdftext))
      (:out pdftext)
      (throw (Exception. (str "Non zero readpdf exit code: " (:exit pdftext)))))))
 