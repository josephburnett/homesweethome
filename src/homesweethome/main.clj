(ns homesweethome.main
  (:require [homesweethome.web.app :refer [app]]
            [homesweethome.data.watch :refer [watch-init]]
            [homesweethome.data.entity :refer [entity-init]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn -main [& args]
  (entity-init)
  (watch-init)
  (run-jetty #'app {:port 4663}))
