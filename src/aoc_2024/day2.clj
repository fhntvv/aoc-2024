(ns aoc-2024.day2
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

;--- Day 2: Red-Nosed Reports ---
;Fortunately, the first location The Historians want to search isn't a long walk from the Chief Historian's office.
;While the Red-Nosed Reindeer nuclear fusion/fission plant appears to contain no sign of the Chief Historian,
;the engineers there run up to you as soon as they see you. Apparently, they still talk about the time Rudolph was
;saved through molecular synthesis from a single electron.
;They're quick to add that - since you're already here
; - they'd really appreciate your help analyzing some unusual data from the Red-Nosed reactor.
;You turn to check if The Historians are waiting for you, but they seem to have already divided into groups that are
; currently searching every corner of the facility. You offer to help with the unusual data.
;The unusual data (your puzzle input) consists of many reports, one report per line.
; Each report is a list of numbers called levels that are separated by spaces. For example:
;
;7 6 4 2 1
;1 2 7 8 9
;9 7 6 2 1
;1 3 2 4 5
;8 6 4 4 1
;1 3 6 7 9
;This example data contains six reports each containing five levels.
;The engineers are trying to figure out which reports are safe. The Red-Nosed reactor safety systems can only tolerate levels that are either gradually increasing or gradually decreasing. So, a report only counts as safe if both of the following are true:
;
;The levels are either all increasing or all decreasing.
;Any two adjacent levels differ by at least one and at most three.
;In the example above, the reports can be found safe or unsafe by checking those rules:
;
;7 6 4 2 1: Safe because the levels are all decreasing by 1 or 2.
;1 2 7 8 9: Unsafe because 2 7 is an increase of 5.
;9 7 6 2 1: Unsafe because 6 2 is a decrease of 4.
;1 3 2 4 5: Unsafe because 1 3 is increasing but 3 2 is decreasing.
;8 6 4 4 1: Unsafe because 4 4 is neither an increase or a decrease.
;1 3 6 7 9: Safe because the levels are all increasing by 1, 2, or 3.
;So, in this example, 2 reports are safe.
;
;Analyze the unusual data from the engineers. How many reports are safe?


(defn read-file
  "Reads all lines from the given file and returns them as a sequence."
  [file-path]
  (with-open [reader (io/reader file-path)]
    (doall (line-seq reader))))

(defn parse-reports
  "Parses a sequence of report strings into a sequence of sequences of numbers."
  [reports]
  (map (fn [report]
         (map (fn [num-str] (Integer/parseInt num-str))
              (str/split report #"\s+")))
       reports))

(defn valid-differences?
  "Checks if all adjacent differences in the sequence are between 1 and 3."
  [levels]
  (every? (fn [diff] (<= 1 diff 3))
          (map #(Math/abs (- %1 %2)) (rest levels) levels)))

(defn is-increasing
  "Check if all elements in a sequence are strictly increasing."
  [s]
  (apply < s))

(defn is-decreasing
  "Check if all elements in a sequence are strictly decreasing."
  [s]
  (apply > s))

(defn is-report-valid
  "Validates if a sequence (report) satisfies the safety rules:
   - All levels are either increasing or decreasing.
   - Adjacent differences are between 1 and 3."
  [report]
  (and (valid-differences? report)
       (or (is-increasing report) (is-decreasing report))))

(defn count-safe-reports
  "Counts the number of safe reports in the parsed data."
  [reports]
  (count (filter is-report-valid reports)))

(defn calculate-part-one
  "Calculates, prints, and returns the Day 2 answer."
  []
  (let [file-path "resources/day2_input.txt"
        reports (parse-reports (read-file file-path))
        safe-reports (count-safe-reports reports)]
    (println "Number of safe reports:" safe-reports)
    safe-reports))

;--- Part Two ---
;The engineers are surprised by the low number of safe reports until they realize they forgot to tell you about
; the Problem Dampener.
;The Problem Dampener is a reactor-mounted module that lets the reactor safety systems tolerate a single bad level in
; what would otherwise be a safe report. It's like the bad level never happened!
;Now, the same rules apply as before, except if removing a single level from an unsafe report would make it safe,
; the report instead counts as safe.
;More of the above example's reports are now safe:
;
;7 6 4 2 1: Safe without removing any level.
;1 2 7 8 9: Unsafe regardless of which level is removed.
;9 7 6 2 1: Unsafe regardless of which level is removed.
;1 3 2 4 5: Safe by removing the second level, 3.
;8 6 4 4 1: Safe by removing the third level, 4.
;1 3 6 7 9: Safe without removing any level.
;Thanks to the Problem Dampener, 4 reports are actually safe!
;
;Update your analysis by handling situations where the Problem Dampener can remove a single level from unsafe reports.
; How many reports are now safe?

(defn remove-level
  "Removes the level at the given index from the report."
  [report idx]
  (let [report-vec (vec report)]
    (vec (concat (subvec report-vec 0 idx) (subvec report-vec (inc idx))))))


(defn is-report-safe-with-dampener
  "Checks if a report is safe with the Problem Dampener by removing a single level."
  [report]
  (some is-report-valid
        (map #(remove-level report %) (range (count report)))))

(defn count-safe-reports-with-dampener
  "Counts the number of safe reports with the Problem Dampener."
  [reports]
  (count (filter is-report-safe-with-dampener reports)))

(defn calculate-part-two
  "Calculates, prints, and returns the Day 2 Part Two answer."
  []
  (let [file-path "resources/day2_input.txt"
        reports (parse-reports (read-file file-path))
        safe-reports (count-safe-reports-with-dampener reports)]
    (println "Number of safe reports with the Problem Dampener:" safe-reports)
    safe-reports))
