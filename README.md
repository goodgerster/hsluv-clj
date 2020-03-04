# hsluv-clj

A human-friendly alternative to the HSL color space, in pure Clojure.

Parent project: HSLuv ([website](https://www.hsluv.org/); [GitHub project](https://github.com/hsluv)). This is a port of the [Java implementation](https://github.com/hsluv/hsluv-java/).

HSLuv-clj requires Clojure 1.10 and works on a JVM only.

## Quality status: inadequate

The library is riddled with bugs and is insufficiently tested/documented. Don’t use it.

### Soon to come

* Acceptable quality
* Testing against the HSLuv exemplars
* Property-based testing
* Minimal Clojure dependency
* Better docstrings
* Polymorphism
* ClojureScript compatibility

## Usage

Use any function in the `hsluv-clj.hsluv` namespace to transform colours from one representation to another.

All functions from the reference implementation are available. Function names are in lisp-case, with the word ‘to’ replaced with the string `->` (for example: `hsluvToRgb` is `hsluv->rgb`). All functions are pure.

### Example

**Note**: this example does not yet work correctly, and I am very sorry. `>_>`

```clojure
(require '[hsluv-clj.hsluv :as hsluv])

(def rebeccapurple "#663399")

(hsluv/hex->hsluv rebeccapurple)
;; => [280.8 70.8 32.9]

;; This is identical to Rebecca Purple except ⅓ darker.
(def dark-rebecca [280.8 70.8 21.9])

(hsluv/hsluv->hex dark-rebecca)
;; => "#46216b"
```

## License

Copyright © 2020 Benjamin Francis Goodger.

This library is licensed under the MIT License; see the LICENSE file for details.
