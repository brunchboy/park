(ns park.routes.admin
  "Supports the admin landing page."
  (:require [park.db.core :as db]
            [park.layout :as layout]))

(defn admin-page [request]
  (let [user-id (get-in request [:session :identity :id])]
    (layout/render request "admin.html" (merge (select-keys request [:admin?])
                                               {:user            (db/get-user {:id user-id})
                                                :users           (db/list-users)}))))
