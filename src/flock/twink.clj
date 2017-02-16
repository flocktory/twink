(ns flock.twink
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def SEX {0 nil 1 :male 2 :female})

(defn- read-dict [name]
  (with-open [reader (io/reader (io/resource name))]
    (let [lines (->> (line-seq reader)
                     (map #(str/split % #",") ))
          keys (map keyword (first lines))]
      (doall (map #(apply assoc {} (interleave keys %)) (rest lines))))))

(def names
  (->> (read-dict "name2name.csv")
       (reduce #(if (= "first" (:type %2))
                  (assoc %1 (:name %2) (:name.new %2)) %1)
               {})))


(def sexes (atom {}))

(defn- read-sexes [dict-name]
  (into {}
        (for [{name :name sex :sex.byname} (read-dict dict-name)
              :let [sex-kw (Integer/parseInt sex)]
              :when sex-kw]
          [name sex-kw])))

(def sexes-default (read-sexes "name2sex.csv"))

(defn- sex? [n locale]
  (if-let [s (@sexes locale)]
    (get s n)
    (let [resource-name (format "name2sex_%s.csv" (some-> locale name str/lower-case))]
      (if (io/resource resource-name)
        (let [loc-sexes (read-sexes resource-name)]
          (do (swap! sexes assoc locale loc-sexes)
              (get loc-sexes n)))
        (sexes-default n)))))

(defn capitalize-words
  "Capitalize every word in a string"
  [s]
  (->> (str/split (str s) #"\b")
       (map str/capitalize)
       str/join))

(defn- locale-to-transformer [locale]
  (if (str/ends-with? locale "2")
    (fn [tokens] (->> tokens
                      (partition 2 1)
                      (map #(str/join " " %))))
    identity))

(defn parse
  "Пытается определить пол и имя по строке-имени. Пользуется словариком соответствий
  имен (katya => екатерина) и полов-имен (екатерина => :female)"
  ([n] (parse n "ru"))
  ([n locale]
   (when n
     (->>
      (re-seq #"\p{L}+" n)
      ((locale-to-transformer locale))
      (map str/lower-case)
      (some
       #(let [n2  (if (= "ru" locale) (names %) %)
              sex (or (sex? % locale) (and n2 (sex? n2 locale)))]
          (when (or n2 sex)
            {:first-name (capitalize-words (or n2 %)) :sex (SEX sex)})))))))
