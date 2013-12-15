(ns homesweethome.data.intake
  (:require [clojure-watch :refer [start-watch]]
            [homesweethome.data.config :refer [read-config]]))

(defn entity-watches [entities]
  (map #()
       entities))

(defn init []
  (let [entities (get (read-config) "entities")]))