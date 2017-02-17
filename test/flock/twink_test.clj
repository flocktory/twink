(ns flock.twink-test
  (:require [clojure.test :refer :all]
            [flock.twink :refer [parse]]))

(deftest parse-name-without-locale-preference
  ;; male
  (is (= (parse "петя")
         {:first-name "Петр" :sex :male :locale :ru}))
  (is (= (parse "Александр")
         {:first-name "Александр" :sex :male :locale :ru}))
  ;; female
  (is (= (parse "ира")
         {:first-name "Ирина" :sex :female :locale :ru}))
  (is (= (parse "евгения")
         {:first-name "Евгения" :sex :female :locale :ru}))
  ;; unisex
  (is (= (parse "женя")
         {:first-name "Женя" :sex nil :locale :ru})))

(deftest parse-name-from-random-input
  (is (= (parse "kolya1985")
         {:first-name "Николай" :sex :male :locale :ru}))
  (is (= (parse "Masha Gorbunova")
         {:first-name "Мария" :sex :female :locale :ru}))
  (is (= (parse "пётр, Абулгары  Эрнест")
         {:first-name "Петр" :sex :male :locale :ru}))
  (is (= (parse "Абулгары  Эрнест!")
         {:first-name "Эрнест" :sex :male :locale :ru}))
  (is (= (parse "natasha иванова")
         {:first-name "Наталья" :sex :female :locale :ru}))
  (is (= (parse "olga.petrov@yandex.ru")
         {:first-name "Ольга" :sex :female :locale :ru}))
  (is (= (parse "dark.imperior@live.com")
         nil))
  (is (= (parse "Иногда Тут Бывает Так")
         nil)))

(deftest parse-name-from-translit
  (is (= (parse "petya")
         {:first-name "Петр" :sex :male :locale :ru}))
  (is (= (parse "olya")
         {:first-name "Ольга" :sex :female :locale :ru}))
  (is (= (parse "zhenya")
         {:first-name "Женя" :sex nil :locale :ru})))

(deftest parse-non-russian-names
  (is (= (parse "Pablo")
         {:first-name "Pablo" :sex :male :locale :es2}))
  (is (= (parse "paulino")
         {:first-name "Paulino" :sex :female :locale :es2}))
  ;; tr
  (is (= (parse "Nilüfer Ivanov")
         {:first-name "Nilüfer" :sex :female :locale :tr}))
  (is (= (parse "rıdvan.2001")
         {:first-name "Rıdvan" :sex :male :locale :tr}))
  ;; мимо
  (is (= (parse "ladyboy")
         nil))
  ;; двойные испанские имена
  (is (= (parse "luis pablo",)
         {:first-name "Luis Pablo" :sex :male :locale :es2})))

(deftest parse-with-preferred-locale
  ;; без локали, будет русское женское имя, потому что есть в name2sex_ru
  (is (= (parse "elia")
         {:first-name "Elia" :sex :female :locale :ru}))
  ;; с приоритетной локалью es2 будет мужское имя Elia
  ;; The name Elia is a baby boy name. The name Elia comes from the Spanish
  ;; origin. In Spanish The meaning of the name Elia is: My God is Jehovah.
  ;; Variant of Hebrew Elijah.
  (is (= (parse "elia" :es2)
         {:first-name "Elia" :sex :male :locale :es2}))

  (is (= (parse "Juan")
         {:first-name "Juan" :sex :female :locale :es2}))
  (is (= (parse "Juan" "es")
         {:first-name "Juan" :sex :male :locale :es})))
