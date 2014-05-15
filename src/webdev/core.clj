(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

(defn greet [req]
  {:status 200
   :body "Hello, World! (now with automatic reload!)"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye, Cruel World!"
   :headers {}})

(defn about [req]
  {:status 200
   :body "My name is microamp. This is my first Ring (Clojure) web app. Be nice. :)"
   :headers {}})

(defn yo [req]
  {:status 200
   :body (str "Yo, " (get-in req [:route-params :name]) "!")
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
       :body (str "Invalid operator: " op)
       :headers {}}
      {:status 200
       :body (str a " " op " " b " = " ((get op-map op) (Integer. a) (Integer. b)))
       :headers {}})))

(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/about" [] about)
  (GET "/request" [] handle-dump)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:a/:op/:b" [] calc)
  (not-found "Page not found."))

(defn -main [port]
  (jetty/run-jetty app
                   {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app)
                   {:port (Integer. port)}))
