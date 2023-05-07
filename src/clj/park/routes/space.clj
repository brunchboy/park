(ns park.routes.space
  "Supports viewing of parking spaces."
  (:require
   [park.config :refer [env]]
   [park.db.core :as db]
   [park.layout :as layout])
  (:import
   (java.util UUID)))

(defn space-page [{:keys [path-params session] :as request}]
  (let [user-id   (get-in session [:identity :id])
        spaces    (db/list-spaces-for-user {:user user-id})
        space-id   (UUID/fromString (:id path-params))
        space      (db/get-space {:id space-id})]
    (if (and space (some #(= (:id %) space-id) spaces))
      (layout/render request "space.html"
                     (merge (select-keys request [:admin?])
                            {:cdn    (env :cdn-url)
                             :user   (db/get-user {:id user-id})
                             :spaces  spaces
                             :space   space}))
      (layout/error-page {:status 404 :title "404 - Page not found"}))))
