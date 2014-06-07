(ns webdev.item.model
  (:require [clojure.java.jdbc :as jdbc]))

(defn create-table [db]
  (jdbc/execute!
   db
   ["CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\""])
  (jdbc/execute!
   db
   ["CREATE TABLE IF NOT EXISTS item
      (id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
      name TEXT NOT NULL,
      description TEXT NOT NULL,
      checked BOOLEAN NOT NULL DEFAULT FALSE,
      created TIMESTAMPTZ NOT NULL DEFAULT now())"]))
