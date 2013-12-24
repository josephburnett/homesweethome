(ns homesweethome.data.hsh
  (:require [cheshire.core :refer [parse-string generate-string]]))

(def hsh-prefix "homesweethome-1.0\n")

(defn read-hsh [f]
  (let [contents (slurp f)]
    (if (.startsWith contents hsh-prefix)
      (parse-string (.substring contents (.length hsh-prefix)) true)
      (throw (Exception. "Not a homesweethome file")))))

(defn write-hsh [f d]
  (let [contents (generate-string d)]
    (spit f (str hsh-prefix contents))))
