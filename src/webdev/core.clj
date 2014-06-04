(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

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

(defroutes app
  (GET "/" [] greet)
  (GET "/about" [] about)
  (GET "/hello/:name" [] hello)
  (GET "/calc/:op/:a/:b" [] calc)
  (GET "/request" [] handle-dump)
  (not-found "Page not found."))

(defn -main [port]
  (jetty/run-jetty app
                   {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app)
                   {:port (Integer. port)}))
