(ns homesweethome.data.hsh
  (:require [cheshire.core :refer [parse-string generate-string]]
            [clojure.java.io :refer [as-file]]
            [clojure.string :refer [join]]
            [me.raynes.fs :refer [mkdirs split exists? create expand-home]])
  (:import [java.io File]))

(def hsh-prefix "homesweethome-1.0\n")

(defn read-hsh [f]
  (let [contents (slurp f)]
    (if (.startsWith contents hsh-prefix)
      (parse-string (.substring contents (.length hsh-prefix)) true)
      (throw (Exception. "Not a homesweethome file")))))

(defn write-hsh [filename data]
  (let [folder (join File/separator (drop-last (split filename)))
        file (as-file filename)
        contents (generate-string data)]
    (if (not (exists? folder)) 
      (do 
        (println "Making folder " folder)
        (mkdirs folder)))
    (if (not (exists? file)) 
      (do 
        (println "Making file " filename)
        (create file)))
    (spit file (str hsh-prefix contents))))
