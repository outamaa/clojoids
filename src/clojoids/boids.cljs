(ns clojoids.boids
  (:require [clojoids.math :as m]))

(def neighbor-radius 0.1)

(defn make-boid [& {:keys [loc vel]
                    :or {loc [(rand) (rand)]
                         vel [(- 0.5 (rand 1.0)) (- 0.5 (rand 1.0))]}}]
  {:loc loc :vel vel})

(defn dist-1D [x0 x1]
  (let [dx (- x1 x0)]
    (if (> (Math/abs dx) 0.5)
      (- dx (Math/sign dx))
      dx)))

(defn dist [[x0 y0] [x1 y1]]
  [(dist-1D x0 x1) (dist-1D y0 y1)])


;; Behaviors - given a boid return a force (acceleration in case of unit mass boids)

(defn separation [neighbors boid]
  (if (not-empty neighbors)
    (let [boid-location (:loc boid)
          inv-dists (map (comp m/v-inv
                               #(dist (:loc %) boid-location)) neighbors)
          avg-inv-dist (apply m/v-avg inv-dists)]
      (m/v* (m/v 0.005) avg-inv-dist))
    [0 0]))

(defn alignment [neighbors boid]
  (if (not-empty neighbors)
    (let [avg-heading (m/v-to-length (apply m/v-avg (map :vel neighbors)) 1.0)
          own-heading (m/v-to-length (:vel boid) 1.0)
          vel-diff (m/v- avg-heading own-heading)]
      (m/v* (m/v 0.05) vel-diff))
    [0 0]))

(defn cohesion [neighbors boid]
  (if (not-empty neighbors)
    (let [boid-location (:loc boid)
          displacement (apply m/v-avg
                              (map #(dist boid-location (:loc %))
                                   neighbors))]
      (m/v* (m/v 1.0) displacement))
    [0 0]))

(defn behaviors [neighbor-boids]
  [(partial separation neighbor-boids)
   (partial alignment neighbor-boids)
   (partial cohesion neighbor-boids)])


;; Integration

(defn integrate-boid [boids delta boid]
  (let [v0 (:vel boid)
        x0 (:loc boid)
        neighbors (filter #(and (not= boid %)
                                (< (m/v-length (dist (:loc %) x0))
                                   neighbor-radius))
                          boids)
        bs (behaviors neighbors)
        a0 (reduce m/v+ ((apply juxt bs) boid))
        t-delta (m/v delta)
        v1 (m/v-clamp (m/v+ v0 (m/v* a0 t-delta)) 1.0)
        x1 (m/v+ x0 (m/v* v1 t-delta))]
    {:loc x1 :vel v1}))

