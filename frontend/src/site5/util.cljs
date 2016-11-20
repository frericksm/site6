(ns site5.util
  (:require [om.next :as om]))

(defn mlog [& messages]
  ;; TODO disable in prod
  (when true
    (apply println messages)))

(defn update-input! [e component key]
  (om/update-state! component assoc key (-> e .-target .-value)))

(defn dissoc-in
  "Dissociates an entry from a nested associative structure returning a new
  nested structure. keys is a sequence of keys. Any empty maps that result
  will not be present in the new structure."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))

(defn remove-from-vector [v item]
  (into [] (remove #{item}) v))
