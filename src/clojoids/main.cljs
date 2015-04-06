(ns clojoids.main
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-time.core :as t]
            [cljs-time.coerce :as tc]
            [clojoids.boids :as b]
            [clojoids.graphics :as g]
            [clojoids.math :as m]
            [figwheel.client :as fw :include-macros true]))

(def width 600)
(def height 600)

(def state
  (atom {:time (t/now)
         :boids (take 20 (repeatedly #(b/make-boid)))}))

(defn constrain-dimensions [boid]
  (update-in boid [:loc] #(m/v-mod % [1.0 1.0])))

(defn update-boid [boids delta boid]
  (let [neighbor-boids (filter #(not= boid %) boids)]
    ((comp constrain-dimensions
           (partial b/integrate-boid boids delta)) boid)))

(defn update-state [state]
  (let [now (t/now)
        delta (/ (- (tc/to-long now) (tc/to-long (:time state))) 1000.0)
        boids (:boids state)]
    {:time now
     :boids (map (partial update-boid boids delta) boids)}))

(defn clojoids []
  (g/clojoids width height (map :loc (:boids @state))))

(defn ^:export start []
  (js/setInterval (fn [] (swap! state update-state)) 50)
  (reagent/render-component [clojoids] (.getElementById js/document "clojoids")))

(fw/watch-and-reload
 :jsload-callback (fn [] (start)))





