(ns webdev.item.handler
  (:require [webdev.item.model :refer [create-item
                                       read-items
                                       update-item
                                       delete-item]])
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

(defn handle-delete-item [req]
  (let [db (:webdev/db req)
        item-id (java.util.UUID/fromString (get-in req [:route-params :item-id]))
        deleted? (delete-item db item-id)]
    (if deleted?
      {:status 302
       :headers {"Location" "/item"}
       :body ""}
      {:status 404
       :headers {}
       :body "Item not found"})))

(defn handle-update-item [req]
  (let [db (:webdev/db req)
        item-id (java.util.UUID/fromString (get-in req [:route-params :item-id]))
        checked (get-in req [:params "checked"])
        updated? (update-item db item-id (= "true" checked))]
    (if updated?
      {:status 302
       :headers {"Location" "/item"}
       :body ""}
      {:status 404
       :headers {}
       :body "Item not found"})))
