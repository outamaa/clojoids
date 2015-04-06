(defproject clojoids "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3169"]
                 [com.andrewmcveigh/cljs-time "0.3.3"]
                 [figwheel "0.2.5"]
                 [reagent "0.5.0"]]
  :plugins [[lein-cljsbuild "1.0.5"]
            [lein-figwheel "0.2.5"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :compiler {:optimizations :none
                                   :source-map true
                                   :preamble ["reagent/react.js"]
                                   :output-to "resources/public/js/compiled/clojoids.js"
                                   :output-dir "resources/public/js/compiled/out"}}
                       ]}
  )
