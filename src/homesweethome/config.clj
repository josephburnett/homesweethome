(ns homesweethome.config
  (:require [homesweethome.data.hsh :refer [read-hsh write-hsh]]
            [me.raynes.fs :refer [file expand-home with-cwd]]))

(defn- read-config []
  (let [config (read-hsh (expand-home "~/.homesweethome.hsh"))]
    (if (.equals "config" (:type config))
      config
      (throw (Exception. "Not a config")))))

(def config (read-config))

(defn cwd-file [path]
  (let [cwd (get-in config [:cwd])]
    (with-cwd cwd
      (file path))))

(defn entity-path [type]
 (let [ep (get-in config [:entities type :path])
       f (cwd-file ep)]
   (.getAbsolutePath f)))

(defn cache-path []
  (let [cache (get-in config [:cache])
        f (cwd-file cache)]
    (.getAbsolutePath f)))
