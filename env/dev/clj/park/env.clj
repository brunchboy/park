(ns park.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [park.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[park started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[park has shut down successfully]=-"))
   :middleware wrap-dev})
