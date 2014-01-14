(ns homesweethome.image.manip
  (:require [clojure.java.io :refer [as-file]])
  (:import [java.io File]
           [java.lang Math]
           [javax.imageio ImageIO]
           [java.awt.image BufferedImage WritableRaster]
           [java.awt Graphics2D Color]
           [com.mortennobel.imagescaling ResampleOp]
           [org.apache.pdfbox.pdmodel PDDocument]))

(set! *warn-on-reflection* true)

(defn y-offsets
  ([coll] (y-offsets coll 0))
  ([coll index]
    (if (empty? coll) '()
      (cons index (y-offsets (rest coll) (+ index (.getHeight (first coll))))))))

(defn x-offsets
  ([coll]
    (let [max-width (apply max (map #(.getWidth %) coll))]
      (map #(/ (- max-width (.getWidth %)) 2)
           coll))))

(defn pdf-to-images [filename]
  (with-open [document (PDDocument/load (as-file filename))]
    (doall
      (map #(.convertToImage %)
           (.getAllPages (.getDocumentCatalog document))))))

(defn fill! [image]
  (let [graphics (.createGraphics image)]
    (do
      (.setPaint graphics (Color. 255 255 255))
      (.fillRect graphics 0 0 (.getWidth image) (.getHeight image)))
    image))

(defn blank-image [width height]
  (fill! (BufferedImage. width height 1)))

(defn stack [images]
  (let [width (apply max (map #(.getWidth %) images))
        height (reduce + (map #(.getHeight %) images))
        image (blank-image width height)]
    (doall
      (map #(insert! %1 image %2 %3)
           images
           (x-offsets images)
           (y-offsets images)))
    image))

(defn load-image [^String f]
  "Load filename f into a BufferedImage"
  (ImageIO/read
   (File. f)))

(defn save-image [^BufferedImage b ^String f]
  "Save image b to filename f."
  (ImageIO/write b "jpg" (File. f)))

(defn rescale [x y ^BufferedImage b]
  "Rescale image b to dimensions x y."
  (. (ResampleOp. x y) (filter b nil)))

(defn rescale-fixed-ratio [x ^BufferedImage b]
  "Resize image b to width x, keeping the aspect ratio."
  (let [h (.getHeight b)
	r (/ x (.getWidth b))
	y (int (* h r))]
    (rescale x y b)))

(defn sub-image [[x y dx dy] ^BufferedImage b]
  "Crop image b to dimensions [x y dx dy]."
  (.getSubimage b x y dx dy))

(defn get-samples [^BufferedImage b]
  "Get a flat list of all samples in image b."
  (let [d (.getData b)]
    (for [x (range (.getWidth d))
	  y (range (.getHeight d))
	  z (range (.getNumBands d))]
      (.getSample d x y z))))

(defn- floor [n x]
  "Round x down to the nearest multiple of n."
  (int (* n (Math/floor (/ x n)))))
  
(defn image-floor [n ^BufferedImage b]
  "Crop image b to the largest width and height
   which are multiples of n."
  (let [x (.getWidth b)
	y (.getHeight b)]
    (.getSubimage b 0 0 (floor n x) (floor n y))))

(defn insert! [^BufferedImage a
	       ^BufferedImage b
	       dx dy]
  "Insert image a into image b at coordinates dx dy."
  (let [^WritableRaster da (.getRaster a)
	^WritableRaster db (.getRaster b)]
    (dorun
     (for [x (range 0 (.getWidth da))
	   y (range 0 (.getHeight da))
	   z (range 0 (.getNumBands da))]
       (.setSample db (int (+ dx x)) (int (+ dy y)) (int z)
		   (.getSample da x y z))))))
