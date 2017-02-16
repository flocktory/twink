# twink

[ladyboy](https://github.com/flocktory/ladyboy), но на кложе

## Usage

Добавить зависимость в `project.clj`:

`[flock/twink "0.1.0"]`

Use in your code:

```lang:clojure
(require '[flock.twink :as twink])
(twink/parse "zhenya") ;; неявная локаль "ru"
; {:first-name "Женя" :sex nil}
(twink/parse "kolya" "ru")
; {:first-name "Николай" :sex :male}
(twink/parse "recep" "tr")
; {:first-name "Recep" :sex :male}
(twink/parse "juan pablo" "es2") ;; двойные испанские имена
; {:first-name "Juan Pablo" :sex :male}
```
