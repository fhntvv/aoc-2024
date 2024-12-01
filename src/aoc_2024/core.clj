(ns aoc-2024.core
  (:require [aoc-2024.day1 :as day1]))


(defn -main
  "run all days"
  [& _args]
  (println "Calling day1")
  (day1/part-one)
  (day1/part-two)
  )
