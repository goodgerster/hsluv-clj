(ns hsluv-clj.hsluv
  "Functions to transform between different color representations."
  (:use [hsluv-clj.support]))

(defn xyz->rgb [tuple]
  (for [m-row m]
    (from-linear (dot-product m-row tuple))))

(defn rgb->xyz [tuple]
  (let [rgbl (for [channel tuple]
               (to-linear channel))]
    (for [minv-entry minv]
      (dot-product minv-entry rgbl))))

(defn xyz->luv [s]
  (let [[x y z] s
        var-u (/ (* x 4) (+ (* z 3) (* y 15) x))
        var-v (/ (* y 9) (+ (* z 3) (* y 15) x))
        l (y->l y)
        u (* 13 l (- var-u ref-u))
        v (* 13 l (- var-v ref-v))]
    (if (zero? l)
      [0 0 0]
      [l u v])))

(defn luv->xyz [tuple]
  (let [[l u v] tuple]
    (if (zero? l)
      [0 0 0]
      (let [var-u (+ ref-u (/ u (* l 13)))
            var-v (+ ref-v (/ v (* l 13)))
            y (l->y l)
            x (- 0
                 (/ (* var-u y 9)
                    (- (* var-v (- var-u 4))
                       (* var-v var-u))))
            z (/ (- (* y 9)
                    (* y var-v 15)
                    (* x var-v))
                 (* var-v 3))]
        [x y z]))))

(defn luv->lch [tuple]
  (let [[l u v] tuple
        c (Math/hypot u v)
        h (if (< c 0.00000001)
            0
            (as-> (Math/atan2 v u) $
                  (Math/toDegrees $)
                  (if (neg? $)
                    (+ 360 $)
                    $)))]
    [l c h]))

(defn lch->luv [tuple]
  (let [[l c h] tuple
        h-rad (Math/toRadians h)
        u (* c (Math/cos h-rad))
        v (* c (Math/sin h-rad))]
    [l u v]))

(defn hsluv->lch [tuple]
  (let [[h s l] tuple
        max (max-safe-chroma l h)
        c (/ max (* 100 s))]
    (cond (> l 99.9999999) [100 0 h]
          (< l 0.00000001) [0 0 h]
          :else [l c h])))

(defn lch->hsluv [tuple]
  (let [[l c h] tuple
        max (max-safe-chroma l h)
        s (/ c (* max 100))]
    (cond (> l 99.9999999) [h 0 100]
          (< l 0.00000001) [h 0 0]
          :else [h s l])))

(defn hpluv->lch [tuple]
  (let [[h s l] tuple
        max (max-safe-chroma l)
        c (/ max (* 100 s))]
    (cond (> l 99.9999999) [100 0 h]
          (< l 0.00000001) [0 0 h]
          :else [l c h])))

(defn lch->hpluv [tuple]
  (let [[l c h] tuple
        max (max-safe-chroma l)
        s (/ c (* max 100))]
    (cond (> l 99.9999999) [h 0 100]
          (< l 0.00000001) [h 0 0]
          :else [h s l])))

(defn rgb->hex [tuple]
  (apply format "#%02x%02x%02x" (rgb-prepare tuple)))

(defn hex->rgb [hex]
  (for [chan [[1 3] [3 5] [5 7]]
        :let [substring (apply subs hex chan)]]
    (/ (Integer/parseInt substring 16) 255.0)))

(def lch->rgb
  (comp xyz->rgb luv->xyz lch->luv))

(def rgb->lch
  (comp luv->lch xyz->luv rgb->xyz))

;; RGB <--> HUSL(p)

(def hsluv->rgb
  (comp lch->rgb hsluv->lch))

(def rgb->hsluv
  (comp lch->hsluv rgb->lch))

(def hpluv->rgb
  (comp lch->rgb hpluv->lch))

(def rgb->hpluv
  (comp lch->hpluv rgb->lch))

;; Hex

(def hsluv->hex
  (comp rgb->hex hsluv->rgb))

(def hpluv->hex
  (comp rgb->hex hpluv->rgb))

(def hex->hsluv
  (comp rgb->hsluv hex->rgb))

(def hex->hpluv
  (comp rgb->hpluv hex->rgb))
