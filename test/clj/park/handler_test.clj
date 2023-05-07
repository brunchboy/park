(ns park.handler-test
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [park.config]
    [park.db.core]
    [park.handler :refer :all]
    [park.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'park.config/env
                 #'park.handler/app-routes
                 #'park.db.core/*db*)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 302 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))
