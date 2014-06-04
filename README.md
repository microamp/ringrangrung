# ringrangrung

Personal experiments with Clojure Ring/Compojure

## Quickstart (notes to self)

* Start a new project (``lein new webdev``)
* Update dependencies in project.clj (e.g. Ring, Compojure etc.)
* Main function (make sure its location is specified in project.clj)
  * Adaptor (e.g. ``ring.adaptor.jetty``)
  * Middleeware (e.g. ``ring.middleware.reload``)
  * Handlers (e.g. ``ring.handler.dump``)
  * Routes (defined using Compojure's ``defroutes``)
* Run the application (``lein run 8000``)

## Deployment (on Heroku)

* Requirements
  * Heroku account
  * Heroku Toolbelt
  * Procfile
* Additional dependencies (in project.clj)
  * Leiningen version (``:min-lein-version``)
  * Jar file (``:uberjar-name``)
* Create an app on Heroku (``heroku create ringrangrung``)
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
