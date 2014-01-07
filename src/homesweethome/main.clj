(ns homesweethome.main
  (:require [homesweethome.web.app :refer [app]]
            [homesweethome.data.watch :refer [watch-init]]
            [homesweethome.data.entity :refer [entity-init]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn -main [& args]
  (let [operation (first args)]
    (case operation
      "intake" (do
                 (entity-init)
                 (watch-init))
      "website" (run-jetty #'app {:port 4663})
      (throw (Exception. "Usage: homesweethome [ intake | website ]")))))
