(ns webdev.item.handler
  (:require [webdev.item.model :refer [create-item
                                       read-items]])
  (:require [webdev.item.view :refer [item-page]])
)

(defn handle-index-items [req]
  (let [db (:webdev/db req)
        items (read-items db)]
    {:status 200
     :headers {}
     :body (item-page items)}))

(defn handle-create-item [req]
  (let [db (:webdev/db req)
        name (get-in req [:form-params "name"])
        description (get-in req [:form-params "description"])
        item-id (create-item db name description)]
    {:status 302
     :headers {"Location" "/item"}
     :body ""}))
