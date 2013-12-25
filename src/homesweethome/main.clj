(ns homesweethome.main
  (:require [homesweethome.web.app :refer [app]]
            [homesweethome.data.watch :refer [init]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn -main [& args]
  (init)
  (run-jetty #'app {:port 4663}))
