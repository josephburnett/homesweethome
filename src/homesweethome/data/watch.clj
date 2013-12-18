(ns homesweethome.data.watch
  (:require [clojure-watch.core :refer [start-watch]]
            [homesweethome.data.config :refer [read-config]]
            [homesweethome.data.entities.pdf :refer [intake]]
            [me.raynes.fs :refer [expand-home]]))

(def intake-watches
  {"pdf" {:event-types [:create]
          :callback (fn [event filename] (intake filename))}})

(defn configure-watches [config]
  (map #(let [type (get % "type")
              intake-watch (get intake-watches type)]
          (if (nil? intake-watch)
            (throw (Exception. (str "Invalid watch type: " type)))
            (assoc intake-watch
                   :path (.getAbsolutePath (expand-home (get % "path"))))))
       (get config "intake-watches")))

(defn init []
  (let [watches (configure-watches (read-config))]
    (println "Starting watches: " watches)
    (start-watch watches)))
 