(ns webdev.item.model
  (:require [clojure.java.jdbc :as jdbc]))

(defn create-table [db]
  (jdbc/execute!
   db
   ["CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\""])
  (jdbc/execute!
   db
   [(str "CREATE TABLE IF NOT EXISTS item "
         "(id UUID PRIMARY KEY DEFAULT uuid_generate_v4(), "
         "name TEXT NOT NULL, "
         "description TEXT NOT NULL, "
         "checked BOOLEAN NOT NULL DEFAULT FALSE, "
         "created TIMESTAMPTZ NOT NULL DEFAULT now())")]))

(defn create-item [db name description]
  (:id (first (jdbc/query
               db
               [(str "INSERT INTO item (name, description) "
                     "VALUES (?, ?) "
                     "RETURNING id")
                name description]))))

(defn read-items [db]
  (jdbc/query
   db
   [(str "SELECT id, name, description, checked, created "
         "FROM item "
         "ORDER BY created")]))

(defn update-item [db id checked]
  (= [1] (jdbc/execute!
          db
          [(str "UPDATE item "
                "SET checked = ? "
                "WHERE id = ?")
           checked id])))

(defn delete-item [db id]
  (= [1] (jdbc/execute!
          db
          [(str "DELETE FROM item "
                "WHERE id = ?")
           id])))
