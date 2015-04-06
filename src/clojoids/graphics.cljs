(ns clojoids.graphics )

(defn boid [width height [x y]]
  [:circle {:cx (* width x)
            :cy (* height y)
            :r "3"
            :stroke "black"
            :stroke-width "1"
            :fill "red"}])

(defn clojoids [width height boid-locations]
  (let [circles (map (partial boid width height) boid-locations)]
   (vec (concat[:svg {:height height :width width}] circles))))
