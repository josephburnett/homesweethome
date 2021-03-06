(defproject homesweethome "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [liberator "0.9.0"]
                 [compojure "1.1.3"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [cheshire "5.2.0"]
                 [me.raynes/fs "1.4.5"]
                 [clojure-watch "0.1.9"]
                 [hiccup "1.0.4"]
                 [digest "1.4.3"]
                 [com.mortennobel/java-image-scaling "0.8.5"]
                 [org.apache.pdfbox/pdfbox "1.8.3"]]
  :main homesweethome.main)
