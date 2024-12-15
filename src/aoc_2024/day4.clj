(ns aoc-2024.day4
  (:require [clojure.string :as str]))

;--- Day 4: Ceres Search ---
;"Looks like the Chief's not here. Next!" One of The Historians pulls out a device and pushes the only button on it.
; After a brief flash, you recognize the interior of the Ceres monitoring station!
;As the search for the Chief continues, a small Elf who lives on the station tugs on your shirt;
; she'd like to know if you could help her with her word search (your puzzle input). She only has to find one word: XMAS.

;This word search allows words to be horizontal, vertical, diagonal, written backwards, or even overlapping other words.
; It's a little unusual, though, as you don't merely need to find one instance of XMAS - you need to find all of them.
; Here are a few ways XMAS might appear, where irrelevant characters have been replaced with .:
;
;
;..X...
;.SAMX.
;.A..A.
;XMAS.S
;.X....
;The actual word search will be full of letters instead. For example:
;
;MMMSXXMASM
;MSAMXMSMSA
;AMXSXMAAMM
;MSAMASMSMX
;XMASAMXAMM
;XXAMMXXAMA
;SMSMSASXSS
;SAXAMASAAA
;MAMMMXMMMM
;MXMXAXMASX
;In this word search, XMAS occurs a total of 18 times; here's the same word search again,
; but where letters not involved in any XMAS have been replaced with .:
;
;....XXMAS.
;.SAMXMS...
;...S..A...
;..A.A.MS.X
;XMASAMX.MM
;X.....XA.A
;S.S.S.S.SS
;.A.A.A.A.A
;..M.M.M.MM
;.X.X.XMASX
;Take a look at the little Elf's word search. How many times does XMAS appear?

(defn prep-input [filename]
  (->> (slurp filename)
       (str/split-lines)
       (remove str/blank?)))

(def test-input (prep-input "resources/day4_test_input.txt"))
(def sample-input (prep-input "resources/day4_input.txt"))

(defn convert-to-grid
  "Converts a vector of strings to a vector of vectors of characters"
  [input]
  (mapv vec input))

(defn count-xmas-occurrences-line
  "Counts how many times 'XMAS' appears contiguously in a line of chars.
   Overlapping occurrences are allowed."
  [line]
  (let [target (vec "XMAS")
        target-length (count target)
        line-length (count line)]
    (loop [i 0
           cnt 0]
      (if (<= (+ i target-length) line-length)
        (recur (inc i)
               (if (= (subvec line i (+ i target-length)) target)
                 (inc cnt)
                 cnt))
        cnt))))



(defn backward-vec [v]
  (vec (reverse v)))

(defn mirror-grid [grid]
  (mapv backward-vec grid))

(defn get-diagonals
  "Returns all diagonals (both primary and secondary) of the grid as vectors of characters."
  [grid]
  (let [rows (count grid)
        cols (count (first grid))]
    (concat
      ;; Primary diagonals (top-left to bottom-right)
      (for [k (range (+ rows cols -1))]
        (vec (for [i (range rows)
                   :let [j (- k i)]
                   :when (and (>= j 0) (< j cols))]
               (get-in grid [i j]))))
      ;; Secondary diagonals (top-right to bottom-left)
      (for [k (range (- cols 1) (- rows 1) -1)]
        (vec (for [i (range rows)
                   :let [j (+ k i)]
                   :when (and (>= j 0) (< j cols))]
               (get-in grid [i j])))))))

(defn flip-vertical [grid]
  (vec (reverse grid)))

(defn count-xmas-occurrences-line
  "Counts how many times 'XMAS' appears contiguously in a line of chars.
   Overlapping occurrences are allowed."
  [line]
  (let [target (vec "XMAS")
        target-length (count target)
        line-length (count line)]
    (loop [i 0
           cnt 0]
      (if (<= (+ i target-length) line-length)
        (recur (inc i)
               (if (= (subvec line i (+ i target-length)) target)
                 (inc cnt)
                 cnt))
        cnt))))

(defn calculate-christmas-occurrences-in-grid
  "Calculate the number of occurrences of XMAS in the grid in all directions."
  [grid]
  (let [all-directions [grid                                 ; horizontal L->R
                        (mirror-grid grid)                   ; horizontal R->L
                        (apply mapv vector grid)             ; vertical top->bottom
                        (apply mapv vector (flip-vertical grid)) ; vertical bottom->top
                        (get-diagonals grid)                 ; diagonal top-left->bottom-right
                        (get-diagonals (mirror-grid grid))   ; diagonal top-right->bottom-left
                        (get-diagonals (flip-vertical grid)) ; diagonal bottom-left->top-right
                        (get-diagonals (mirror-grid (flip-vertical grid))) ; diagonal bottom-right->top-left
                        ]]
    (reduce + (for [dir all-directions]
                (reduce + (map count-xmas-occurrences-line dir))))))

(defn calculate-part-one
  []
  (->> sample-input
       convert-to-grid
       calculate-christmas-occurrences-in-grid))
