(defproject gol-clj "0.1.0-SNAPSHOT"
  :description "Me playing around with quil and Conway's Game of Life"
  :url "http://github.com/timothyhahn"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [quil "1.7.0"]]
  :main ^:skip-aot gol-clj.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
