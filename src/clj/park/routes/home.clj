(ns park.routes.home
  "Supports the home page, a few tiny pages and actions, and defines the
  main routing for the application."
  (:require
   [park.layout :as layout]
   [park.db.core :as db]
   [park.routes.admin :as admin]
   [park.routes.login :as login]
   [park.routes.profile :as profile]
   [park.routes.user :as user]
   [clojure.set :as set]
   [clojure.string :as str]
   [park.middleware :as middleware]
   [ring.util.json-response :refer [json-response]]))

(defn home-page [request]
  (let [user-id (get-in request [:session :identity :id])
        spaces  (db/list-spaces-for-user {:user user-id})]
    (layout/render request "home.html" (merge (select-keys request [:active? :admin?])
                                              {:user   (db/get-user {:id user-id})
                                               :spaces spaces}))))

(defn about-page [request]
  (let [user-id (get-in request [:session :identity :id])
        spaces  (db/list-spaces-for-user {:user user-id})]
    (layout/render request "about.html" (merge (select-keys request [:active? :admin?])
                                               {:spaces spaces}))))


(defn wrap-user-flags [handler]
  (fn [request]
    (if-let [id (get-in request [:identity :id])]
      (let [user (db/get-user {:id id})]
        (handler (assoc request :admin? (:admin user))))
      (handler request))))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats
                 wrap-user-flags]}
   ["/" {:get home-page}]
   ["/about" {:get about-page}]
   ["/admin/delete-user/:id" {:get  user/delete-user-page
                               :post user/user-delete}]
   ["/admin/user/" {:get  user/user-page
                     :post user/user-save}]
   ["/admin/user/:id" {:get  user/user-page
                        :post user/user-save}]
   ["/admin/users" {:get user/list-users-page}]
   #_["/admin/user-spaces/:id" {:get user/user-spaces-page}]
   ["/admin" {:get admin/admin-page}]
   ["/login" {:get  login/login-page
              :post login/login-authenticate}]
   ["/logout" {:get login/logout}]
   ["/profile" {:get  profile/profile-page
                :post profile/profile-update}]
   #_["/space/:id" {:get space/space-page}]])
