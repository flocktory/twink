(ns flock.twink-test
  (:require [clojure.test :refer :all]
            [flock.twink :refer [parse]]))

(deftest parse-name-with-default-locale
  ;; male
  (is (= (parse "петя")
         {:first-name "Петр" :sex :male}))
  (is (= (parse "Александр")
         {:first-name "Александр" :sex :male}))
  ;; female
  (is (= (parse "ира")
         {:first-name "Ирина" :sex :female}))
  (is (= (parse "евгения")
         {:first-name "Евгения" :sex :female}))
  ;; unisex
  (is (= (parse "женя")
         {:first-name "Женя" :sex nil})))

(deftest parse-name-from-random-input
  (is (= (parse "kolya1985")
         {:first-name "Николай" :sex :male}))
  (is (= (parse "Masha Gorbunova")
         {:first-name "Мария" :sex :female}))
  (is (= (parse "пётр, Абулгары  Эрнест")
         {:first-name "Петр" :sex :male}))
  (is (= (parse "Абулгары  Эрнест!")
         {:first-name "Эрнест" :sex :male}))
  (is (= (parse "natasha иванова")
         {:first-name "Наталья" :sex :female}))
  (is (= (parse "olga.petrov@yandex.ru")
         {:first-name "Ольга" :sex :female}))
  (is (= (parse "dark.imperior@live.com")
         nil))
  (is (= (parse "Иногда Тут Бывает Так")
         nil)))

(deftest parse-name-from-translit
  (is (= (parse "petya")
         {:first-name "Петр" :sex :male}))
  (is (= (parse "olya")
         {:first-name "Ольга" :sex :female}))
  (is (= (parse "zhenya")
         {:first-name "Женя" :sex nil})))

(deftest parse-name-with-locale
  (is (= (parse "Juan" "es")
         {:first-name "Juan" :sex :male}))
  (is (= (parse "maria" "es")
         {:first-name "Maria" :sex :female}))
  ;; tr
  (is (= (parse "Nilüfer Ivanov" "tr")
         {:first-name "Nilüfer" :sex :female}))
  (is (= (parse "rıdvan.2001" "tr")
         {:first-name "Rıdvan" :sex :male}))
  ;; мимо
  (is (= (parse "ladyboy" "tr")
         nil))
  ;; двойные испанские имена
  (is (= (parse "luis pablo", "es2")
         {:first-name "Luis Pablo" :sex :male})))
