(ns gol-clj.core
  (:require [quil.core :as quil])
  (:gen-class))


;; Some constants I don't want.
(def WIDTH 50)
(def HEIGHT 50)
(def PERCENTAGE 80)

(defn random-board 
  "Creates board of width and height filled with a percentage filled"
  [width height]
  (take height (repeatedly  ;; For h times
    (fn [] (map #(if (> % PERCENTAGE) true false)
      (take width (repeatedly (fn [] (rand-int 100))))))))) ;; For w times

(defn lookup-board 
  "Creates a board you can use to look up a node's position.
  TODO: replace this function and the following function by just giving each node it's own position."
  [board]
  (map-indexed vector 
    (map #(map-indexed vector %) board)))

(defn lookup 
  "Look up given x, y, and a board, whether or not the position is alive."
  [x y board]
  (if (or (< x 0) ;; Out of bounds 
          (< y 0)
          (> x (dec (count board)))
          (> y (dec (count (first board)))))
    false
    (nth (nth (nth (nth (lookup-board board) x) 1) y) 1))) ;; Messy way of accessing the state at that location 

(defn neighbors 
  ;; Creates a vector of neighbors
  [x y]
  (for [dx [-1 0 1]
        dy [-1 0 1]
        :when (not (and (= dx 0) (= dx dy)))]
    [(+ x dx) (+ y dy)]))

(defn is-dead?
  ;; Predicate that give an x/y value with a board returns whether or not that cell is alive
  [[x y] board]
  (if (lookup x y board)
    true
    false))

(defn count-neighbors 
  "Counts the number of neighbors at a given point."
  [x y board]
  (count (filter true? (pmap #(is-dead? % board) (neighbors x y)))))

;; TODO: Get rid of atom
(def b (atom (random-board WIDTH HEIGHT)))

;; glider
;(def b (atom (concat [(concat [false true false] (take 47 (repeat false)))
;              (concat [false false true] (take 47 (repeat false)))
;              (concat [true true true] (take 47 (repeat false)))]
;                     (take 47 (repeat (take 50 (repeat false)))))))

(defn step 
  "Increments the game of life by a single step for a given board."
  [board]
  (for [x (range (count board))] ;; Could get rid of all these for loops if I didn't just use a 2D vector
    (for [y (range (count (first board)))] ;; Probably something I could do and will be assisted by the above TODOs
      (if (lookup x y board) 
        (condp = (count-neighbors x y board)
        2 true
        3 true
        false)
        (condp = (count-neighbors x y board)
          3 true
          false)))))

(defn setup []
  (quil/smooth)
  (quil/frame-rate 60)
  (quil/background 255))

(defn draw []
  (quil/stroke 0)
  (quil/stroke-weight 0)
  (quil/fill 0)
  (quil/background 255)
  (swap! b step)
  (doall 
    (for [x (range WIDTH)
        y (range HEIGHT)]
      (if (lookup x y @b)
        (quil/rect (* x 10) 
              (* y 10)
              10
              10)))))


(quil/defsketch example
  :title "Game of Life"
  :setup setup
  :draw draw
  :size [(* WIDTH 10) (* HEIGHT 10)])


(defn -main []
  (println "Let's roll"))
