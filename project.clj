(defproject todomvc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.9.6"]
                 [ring/ring-jetty-adapter "1.9.6"]
                 [metosin/reitit-core "0.5.18"]
                 [metosin/reitit-ring "0.5.18"]
                 [hiccup "1.0.5"]]
  :main ^:skip-aot todomvc.core
  :target-path "target/%s"
  :plugins [[lein-cljfmt "0.9.0"]]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
