# ringrangrung

Personal experiments with Clojure Ring/Compojure

## Quickstart (notes to self)

* Start a new project (``lein new webdev``)
* Update dependencies in project.clj (e.g. Ring, Compojure, JDBC etc.)
* core.clj (make sure the main function is specified in project.clj)
  * Adaptor
    * ``ring.adaptor.jetty``
  * Middleware
    * ``wrap-reload`` (for automatic reload)
    * ``wrap-params`` (for parsing parameters)
    * ``wrap-resource`` (for static files)
    * ``wrap-file-info`` (for static files)
    * other custom middleware for simulating PUT/DELETE methods, passing on constants such as database connection string
  * Handlers
    * ``ring.handler.dump`` (for viewing request in depth)
    * other custom handlers
  * Routes (defined using Compojure's ``defroutes``)
  * Views (HTML generation) using Hiccup
* Run the application (``lein run 8000``)

## Deployment (on Heroku)

* Requirements
  * Heroku account
  * Heroku Toolbelt
  * Procfile
* Additional dependencies (in project.clj)
  * Leiningen version (``:min-lein-version``)
  * Jar file (``:uberjar-name``)
* Heroku setup (with PostgreSQL)
  * ``heroku create ringrangrung`` (new app)
  * ``heroku addons:add heroku-postgresql:hobby-dev`` (new database)
  * ``heroku pg`` (view databases)
  * ``heroku pg:promote HEROKU_POSTGRESQL_XXX_URL``
  * ``heroku config``
* Git setup
  * ``git init``
  * ``git add .``
  * ``git commit -m "Initial commit"``
  * ``git push heroku master`` to push to Heroku (check .git/config)
  * ``git push github master`` to push to Github (check .git/config)

## License

Copyright © 2014 James Nah

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
