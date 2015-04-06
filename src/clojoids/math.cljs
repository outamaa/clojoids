(ns clojoids.math)

(defn v
  ([x] [x x])
  ([x y] [x y]))

(defn v+ [vec1 & vecs]
  (vec (reduce #(map + %1 %2) vec1 vecs)))

(defn v- [vec1 & vecs]
  (vec (reduce #(map - %1 %2) vec1 vecs)))

(defn v* [[x0 y0] [x1 y1]]
  (v (* x0 x1) (* y0 y1)))

(defn v-div [[x0 y0] [x1 y1]]
  (v (/ x0 x1) (/ y0 y1)))

(defn v-dot [[x0 y0] [x1 y1]]
  (+ (* x0 x1) (* y0 y1)))

(defn v-length [v]
  (Math/sqrt (v-dot v v)))

(defn v-to-length [x len]
  ((comp (partial v* (v len)) #(v-div % (v (v-length %)))) x))

(defn v-inv [x]
  (let [inv-length (/ 1 (v-length x))]
    (v-to-length x inv-length)))

(defn v-clamp [x len]
  (if (< (v-length x) len)
    x
    (v-to-length x len)))

(defn v-mod [[x y] [mod-x mod-y]]
  (v (mod x mod-x) (mod y mod-y)))

(defn v-round [[x y]]
  (v (Math/round x) (Math/round y)))

(defn v-avg [& vecs]
  (v-div (apply v+ vecs) (v (count vecs))))
