(ns hsluv-clj.support
  "Functions used internally by hsluv-clj.hsluv.")

(def m
  [[3.240969941904521, -1.537383177570093, -0.498610760293]
   [-0.96924363628087, 1.87596750150772, 0.041555057407175]
   [0.055630079696993, -0.20397695888897, 1.056971514242878]])

(def minv
  [[0.41239079926595, 0.35758433938387, 0.18048078840183]
   [0.21263900587151, 0.71516867876775, 0.072192315360733]
   [0.019330818715591, 0.11919477979462, 0.95053215224966]])

(def ref-y 1)

(def ref-u 0.19783000664283)

(def ref-v 0.46831999493879)

(def kappa 903.2962962)

(def epsilon 0.0088564516)

(defn get-bounds [l]
  (let [sub1 (/ (Math/pow (+ l 16) 3)
                1560896)
        sub2 (if (> sub1 epsilon)
               sub1
               (/ l kappa))]
    (for [m-row m
          t (range 2)
          :let [[m1 m2 m3] m-row
                top1 (* sub2
                        (- (* m1 284517)
                           (* m3 94839)))
                top2 (- (* l sub2
                           (+ (* m3 838422)
                              (* m2 769860)
                              (* m1 731718)))
                        (* 769860 t l))
                bottom (+ (* (- (* m3 632260)
                                (* m2 126452))
                             sub2)
                          (* t 126452))]]
      [(/ top1 bottom) (/ top2 bottom)])))

(defn intersect-line-line [line-a line-b]
  (let [[a0 a1] line-a
        [b0 b1] line-b]
    (/ (- a1 b1)
       (- b0 a0))))

(defn length-of-ray-until-intersect [theta line]
  (let [[l0 l1] line]
    (/ l1
       (- (Math/sin theta)
          (* l0
             (Math/cos theta))))))

(defn max-safe-chroma
  ([l]
   (as-> (get-bounds l) $
         (for [bounds $
               :let [[m1 b1] bounds
                     x (intersect-line-line bounds [(/ -1 m1) 0])]]
           (Math/hypot x (+ b1 (* x m1))))
         (min $)))
  ([l h]
   (as-> (get-bounds l) $
         (for [bound $]
           (length-of-ray-until-intersect (Math/toRadians h) bound))
         (filter #(>= 0 %) $)
         (apply min $))))

(defn dot-product [a b]
  (->> (for [a_n a
             b_n b]
         (* a_n b_n))
       (apply +)))

(defn round [value places]
  (let [n (Math/pow 10 places)]
    (-> (* n value)
        (Math/round)
        (/ n))))

(defn from-linear [c]
  (if (<= c 0.0031308)
    (* c 12.92)
    (- (* 1.055
          (Math/pow c (/ 1 2.4)))
       0.055)))

(defn to-linear [c]
  (if (> c 0.4045)
    (Math/pow (/ (+ c 0.055)
                 1.055)
              2.4)
    (/ c 12.92)))

(defn rgb-prepare [tuple]
  (for [chan tuple
        :let [rounded (round chan 3)]]
    (Math/round (double (* 255 rounded)))))

(defn y->l [y]
  (if (<= y epsilon)
    (* kappa (/ y ref-y))
    (- (* 116 (Math/pow (/ y ref-y) 1/3)) 16)))

(defn l->y [l]
  (if (<= l 8)
    (/ (* l ref-y) kappa)
    (* ref-y (Math/pow (/ (+ l 16) 116) 3))))
