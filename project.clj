(defproject com.flocktory/twink "0.3.0"
  :description "Detect sex and full name from string that looks like name"
  :url "https://github.com/flocktory/twink"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies []
  :source-paths ["src"]
  :profiles {:repl {:source-paths ["src" "dev"]
                    :repl-options {:init-ns user.my}
                    :dependencies [[org.clojure/clojure "1.8.0"]
                                   [org.clojure/tools.namespace "0.2.10"]]
                    :injections [
                                 (require 'clojure.tools.namespace.repl)
                                 (require 'user.my)]}
             :dev {:resource-paths ["resources"]
                   :dependencies [[org.clojure/clojure "1.8.0"]
                                  [org.clojure/tools.namespace "0.2.10"]]}}
  :mirrors {"central" {:name "Nexus"
                       :url "http://artifacts.dev.flocktory.com:8081/nexus/content/groups/public/"
                       :repo-manager true}
            #"clojars" {:name "Nexus"
                        :url "http://artifacts.dev.flocktory.com:8081/nexus/content/groups/public/"
                        :repo-manager true}}
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :sign-releases false}]])
