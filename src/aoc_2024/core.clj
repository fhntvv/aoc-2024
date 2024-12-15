(ns aoc-2024.core
  (:require [aoc-2024.day1 :as day1])
  (:require [aoc-2024.day2 :as day2])
  (:require [aoc-2024.day3 :as day3]))

(defn measure-time
  "Measure time of function execution in milliseconds, returning [result time-in-ms]"
  [f]
  (let [start (System/nanoTime)
        result (f)
        end (System/nanoTime)
        elapsed-ms (/ (- end start) 1e6)]
    [result elapsed-ms]))

(defn -main
  "run all days"
  [& _args]
  (doseq [[label f] [["day1 part 1" day1/calculate-part-one]
                     ["day1 part 2" day1/calculate-part-two]
                     ["day2 part 1" day2/calculate-part-one]
                     ["day2 part 2" day2/calculate-part-two]
                     ["day3 part 1" day3/calculate-part-one]
                     ["day3 part 2" day3/calculate-part-two]]]
    (let [[_ time] (measure-time f)]
      (println label "Time in ms:" time))))

