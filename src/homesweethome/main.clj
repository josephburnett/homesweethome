(ns homesweethome.main
  (:require [homesweethome.web.app :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn -main [& args]
  (run-jetty #'app {:port 4663}))
