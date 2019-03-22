(defproject kamera "0.1.1-SNAPSHOT"
  :description "UI testing via image comparison and devcards"
  :url "https://github.com/oliyh/kamera"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "--no-sign"]
                  ["deploy" "clojars"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]
  :dependencies [[me.raynes/conch "0.8.0"]
                 [doo-chrome-devprotocol "0.1.0"]
                 [hickory "0.7.1"]]
  :monkeypatch-clojure-test false
  :profiles {:provided {:dependencies [[org.clojure/clojure "1.10.0"]
                                       [org.clojure/clojurescript "1.10.339"]
                                       [org.clojure/tools.reader "1.3.2"]
                                       [com.bhauman/figwheel-main "0.1.9"]]}
             :dev {:dependencies [;; required for example project
                                  [reagent "0.8.1"]
                                  [devcards "0.2.6"]]
                   :resource-paths ["example/resources"]}})
