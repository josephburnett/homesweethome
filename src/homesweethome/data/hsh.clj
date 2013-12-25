(ns homesweethome.data.hsh
  (:require [cheshire.core :refer [parse-string generate-string]]
            [clojure.java.io :refer [as-file]]
            [me.raynes.fs :refer [exists? create expand-home]]))

(def hsh-prefix "homesweethome-1.0\n")

(defn read-hsh [f]
  (let [contents (slurp f)]
    (if (.startsWith contents hsh-prefix)
      (parse-string (.substring contents (.length hsh-prefix)) true)
      (throw (Exception. "Not a homesweethome file")))))

(defn write-hsh [f d]
  (let [file (as-file f)
        contents (generate-string d)]
    (println f)
    (if (not (exists? file)) (create file))
    (spit file (str hsh-prefix contents))))
