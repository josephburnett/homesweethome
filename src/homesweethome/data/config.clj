(ns homesweethome.data.config
  (:require [homesweethome.data.hsh :refer [read-hsh write-hsh]]
            [me.raynes.fs :refer [expand-home]]))

(defn read-config []
  (let [config (read-hsh (expand-home "~/.homesweethome.hsh"))]
    (if (.equals "config" (get config "type"))
      config
      (throw (Exception. "Not a config")))))
