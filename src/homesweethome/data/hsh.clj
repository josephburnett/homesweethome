(ns homesweethome.data.hsh
  (:require [cheshire.core :refer [parse-string generate-string]]))

(def hsh-prefix "homesweethome-1.0\n")

(defn read-json [s]
  (parse-string s))

(defn write-json [d]
  (generate-string d))

(defn read-hsh [f]
  (let [contents (slurp f)]
    (if (.startsWith contents hsh-prefix)
      (read-json (.substring contents (.length hsh-prefix)))
      (throw (Exception. "Not a homesweethome file")))))

(defn write-hsh [f d]
  (let [contents (generate-string d)]
    (spit f (str hsh-prefix contents))))
