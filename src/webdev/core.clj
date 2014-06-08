(ns webdev.core
  (:require [webdev.item.model :as item]
            [webdev.item.handler :refer [handle-index-items
                                         handle-create-item
                                         handle-delete-item
                                         handle-update-item]])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

(def db (or (System/getenv "DATABASE_URL")
            "jdbc:postgresql://localhost/webdev?user=postgres"))

(defn greet [req]
  {:status 200
   :body "welcome to ringrangrung"
   :headers {}})

(defn about [req]
  {:status 200
   :body "about ringrangrung"
   :headers {}})

(defn hello [req]
  {:status 200
   :body (str "hello, " (get-in req [:route-params :name]))
   :headers {}})

(def op-map {"+" +
             "-" -
             "*" *
             ":" /})

(defn calc [req]
  (let [a (get-in req [:route-params :a])
        op (get-in req [:route-params :op])
        b (get-in req [:route-params :b])
        func (get op-map op)]
    (if (nil? func)
      {:status 404
       :body (str "invalid operator: " op
                  " (must be one of the following: " (apply str (interpose ", " (keys op-map))) ")")
       :headers {}}
      {:status 200
       :body (str "(" op " " a " " b ")\n" ((get op-map op) (Integer. a) (Integer. b)))
       :headers {}})))

(defroutes routes
  "Defines routes"
  (GET "/" [] greet)
  (GET "/about" [] about)
  (GET "/hello/:name" [] hello)
  (GET "/calc/:op/:a/:b" [] calc)
  (ANY "/request" [] handle-dump)
  (GET "/item" [] handle-index-items)
  (POST "/item" [] handle-create-item)
  (DELETE "/item/:item-id" [] handle-delete-item)
  (PUT "/item/:item-id" [] handle-update-item)
  (not-found "Page not found."))

(defn wrap-db [hdlr]
  "Defines a middleware to wrap database connection string"
  (fn [req]
    (hdlr (assoc req :webdev/db db))))

(defn wrap-server [hdlr]
  (fn [req]
    (assoc-in (hdlr req) [:headers "Server"] "RingRangRung")))

(def sim-methods {"PUT" :put
                  "DELETE" :delete})

(defn wrap-simulated-methods [hdlr]
  (fn [req]
    (if-let [method (and (= :post (:request-method req))
                         (sim-methods (get-in req [:params "_method"])))]
      (hdlr (assoc req :request-method method))
      (hdlr req))))

(def app
  "Defines middleware stack"
  (wrap-server
   (wrap-file-info
    (wrap-resource
     (wrap-db
      (wrap-params
       (wrap-simulated-methods
        routes)))
     "static"))))

(defn -main [port]
  (item/create-table db)
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (item/create-table db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
