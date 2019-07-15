# twink

[ladyboy](https://github.com/flocktory/ladyboy), but in clojure: find out whether it boy or girl?

[![Clojars Project](https://clojars.org/com.flocktory/twink/latest-version.svg)](https://clojars.org/com.flocktory/twink)


## Usage

Add it to your `project.clj`:

`[com.flocktory/twink "0.3.1"]`

Use in your code:

```clojure
(require '[flock.twink :as twink])
(twink/parse "zhenya")
; {:first-name "Женя" :sex nil :locale :ru }
(twink/parse "kolya")
; {:first-name "Николай" :sex :male :locale :ru}
(twink/parse "recep")
; {:first-name "Recep" :sex :male :locale :ru}
(twink/parse "juan pablo")n
; {:first-name "Juan Pablo" :sex :male :locale :es2}
(twink/parse "dark emperor")
; nil
(twink/parse "elia" :es) ;; use :es locale first'
; {:first-name "Elia" :sex :male :locale :es}
```
