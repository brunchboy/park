(ns park.util
  "Utility functions."
  (:require [java-time :as jt]
            [park.config :refer [env]]))

(defn same-day?
  "Checks whether the specified event last ran today (in the time zone
  of the building)."
  [event]
  (let [local-timezone (jt/zone-id (get-in env [:location :timezone]))
        event-date     (jt/local-date (jt/with-zone-same-instant (.atZone (:happened event) (jt/zone-id "UTC"))
                                        local-timezone))
        today          (jt/local-date (jt/instant) local-timezone)]
    (= event-date today)))

(defn localize-timestamp
  "Converts a timestamp to a local date and time (if an un-zoned
  Instant, considers it to be in UTC). Accepts either an Instant
  object or a number; if `nil` returns `nil`."
  [timestamp]
  (cond
    (jt/zoned-date-time? timestamp)
    (let [local-timezone (jt/zone-id (get-in env [:location :timezone]))]
      (jt/local-date-time (jt/with-zone-same-instant timestamp local-timezone)))

    (jt/instant? timestamp)
    (let [local-timezone (jt/zone-id (get-in env [:location :timezone]))]
      (jt/local-date-time (jt/with-zone-same-instant (.atZone timestamp (jt/zone-id "UTC")) local-timezone)))

    (number? timestamp)
    (localize-timestamp (jt/instant timestamp))))

(defn format-timestamp-relative
  "Formats a timestamp as a string, describing it relative to today if
  it falls within a week."
  [timestamp]
  (if-let [localized (localize-timestamp timestamp)]
    (let [local-timezone (jt/zone-id (get-in env [:location :timezone]))
          date           (jt/truncate-to (jt/local-date-time localized) :days)
          today          (jt/truncate-to (jt/local-date-time (jt/instant) local-timezone) :days)
          days           (jt/as (jt/duration date today) :days)]
      (str (case days
             0           "Today"
             1           "Yesterday"
             (2 3 4 5 6) (jt/format "EEEE" date)
             (jt/format "YYYY-MM-dd" date))
           (jt/format " HH:mm:ss" localized)))
    "Never"))
