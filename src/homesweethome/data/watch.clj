(ns homesweethome.data.watch
  (:require [clojure-watch.core :refer [start-watch]]
            [homesweethome.config :refer [config entity-path]]
            [homesweethome.data.entities.pdf :refer [intake]]
            [me.raynes.fs :refer [expand-home]]))

(def intake-watches
  {:pdf {:event-types [:create]
         :callback (fn [event filename] (intake filename))
         :options {:recursive true}}})

(defn configure-watches []
  (map #(let [type (first %)
              intake-watch (get intake-watches type)]
          (if (nil? intake-watch)
            (throw (Exception. (str "Invalid watch type: " type)))
            (assoc intake-watch
                   :path (entity-path type))))
       (seq (:entities config))))

(defn watch-init []
  (let [watches (configure-watches)]
    (println "Starting watches: " watches)
    (start-watch watches)))
