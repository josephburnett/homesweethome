(ns homesweethome.data.watch
  (:require [clojure-watch.core :refer [start-watch]]
            [homesweethome.config :refer [config]]
            [homesweethome.data.entities.pdf :refer [intake]]
            [me.raynes.fs :refer [expand-home]]))

(def intake-watches
  {"pdf" {:event-types [:create]
          :callback (fn [event filename] (intake filename))}})

(defn configure-watches []
  (map #(let [type (:type %)
              intake-watch (get intake-watches type)]
          (if (nil? intake-watch)
            (throw (Exception. (str "Invalid watch type: " type)))
            (assoc intake-watch
                   :path (.getAbsolutePath (expand-home (:path %))))))
       (:intake-watches config)))

(defn init []
  (let [watches (configure-watches)]
    (println "Starting watches: " watches)
    (start-watch watches)))
 