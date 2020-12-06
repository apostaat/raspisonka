(defproject raspisonka "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojure "1.8.0"]
                 [environ             "1.1.0"]
                 [morse               "0.2.4"]
                 [cheshire           "5.10.0"]
                 [send-mail           "0.1.0"]
                 [hiccup              "1.0.5"]
                 [clj-pdf             "2.5.4"]
                 [dawran6/emoji       "0.1.5"]
                 [clj-htmltopdf       "0.1-alpha7"]
                 [clj-time "0.15.2"]]
  :plugins [[lein-environ "1.2.0"]]
  :main ^:skip-aot raspisonka.core
  :target-path "target/%s"
  :profiles {:dev {:env {:telegram-token "spikrali"}}
             :uberjar {:aot :all}})
