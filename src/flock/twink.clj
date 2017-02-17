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

(def locales [:ru :tr :es2 :es])
(def double-names-locales #{:es2})

(def russian-names
  (->> (read-dict "name2name.csv")
       (reduce #(if (= "first" (:type %2))
                  (assoc %1 (:name %2) (:name.new %2)) %1)
               {})))

(def names
  {:ru russian-names})

(defn- read-sexes [locale]
  (into {}
        (for [{name :name sex :sex.byname} (read-dict (format "name2sex_%s.csv"
                                                              (name locale)))
              :let [sex-kw (Integer/parseInt sex)]
              :when sex-kw]
          [name sex-kw])))

(def sexes
  (into {} (map (juxt identity read-sexes) locales)))

(defn- sex? [n locale]
  (when n
    (let [s (sexes locale)]
      (get s n))))

(defn- normalize-name [name locale]
  (if-let [table (names locale)]
    (table name)
    ;; else
    nil))

(defn- capitalize-words
  "Capitalize every word in a string"
  [s]
  (->> (str/split (str s) #"\b")
       (map str/capitalize)
       str/join))

(defn- locale-to-transformer [locale]
  (if (double-names-locales locale)
    (fn [tokens] (->> tokens
                      (partition 2 1 "")
                      (map #(str/join " " %))))
    identity))

(defn- tr-locale [locale]
  (cond (keyword? locale)
        locale
        (string? locale)
        (-> locale str/lower-case keyword)
        :else nil))

(defn- try-parse-with-locale [n locale]
  (->>
   (re-seq #"\p{L}+" n)
   ((locale-to-transformer locale))
   (map str/lower-case)
   (some
    (fn [token]
      (let [maybe-normalized (normalize-name token locale)
            sex (or (sex? token locale) (sex? maybe-normalized locale))]
        (when (or maybe-normalized sex)
          {:first-name (capitalize-words (or maybe-normalized token))
           :sex (SEX sex)
           :locale locale}))))))

(defn parse
  "Пытается определить пол и имя по строке-имени. Пользуется словариком соответствий
  имен (katya => екатерина) и полов-имен (екатерина => :female)"
  ([n] (parse n nil))
  ([n locale]
   (when n
     (let [locale' (tr-locale locale)
           ordered-locales (if locale' (distinct (cons locale' locales))
                               locales)]
    (->> ordered-locales
         (map (partial try-parse-with-locale n))
         (some identity))))))
