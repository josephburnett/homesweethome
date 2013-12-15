(ns homesweethome.data.watch
  (:require [clojure-watch.core :refer [start-watch]]
            [homesweethome.data.config :refer [read-config]]
            [homesweethome.data.entities.pdf :refer [intake :as intake-pdf]]))

(def intake-watches
  {"pdf" {:event-types [:create]
          :callback (fn [event filename] (intake-pdf filename))}})

(defn configure-watches [config]
  (map #(let [type (get % "type")
              intake-watch (get intake-watches type)]
          (if (nil? entity-watch)
            (throw (Exception. (str "Invalid watch type: " type)))
            (assoc intake-watch
                   :path (get % "path")))
       (get config "intake-watches"))))

(defn init []
  (let [watches (configure-watches (read-config))]
    (start-watch watches)))
 