# dertwothumber

FIXME: description

## Developing

1. Setup a github application
1. start port forwarding (`bin/ssh-tunnel.sh`)
1. change ./profiles.clj to have:
```clj

{:profiles/dev  {:env {:github-client-id "your client-id"
                       :github-client-secret "your secret"
                       :app-host "host from port forwarding"}}
```
1. modify dev/cljs/user.cljs to point figwheel at your local host (sometimes that local host is your docker host).


### Setup

When you first clone this repository, run:

```sh
lein setup
```

This will create files for local configuration, and prep your system
for the project.

### Environment

To begin developing, start with a REPL.

```sh
lein repl
```

Run `go` to initiate and start the system.

```clojure
user=> (go)
:started
```

By default this creates a web server at <http://localhost:3000>.

When you make changes to your source files, use `reset` to reload any
modified files and reset the server. Changes to CSS or ClojureScript
files will be hot-loaded into the browser.

```clojure
user=> (reset)
:reloading (...)
:resumed
```

If you want to access a ClojureScript REPL, make sure that the site is loaded
in a browser and run:

```clojure
user=> (cljs-repl)
Waiting for browser connection... Connected.
To quit, type: :cljs/quit
nil
cljs.user=>
```

### Testing

Testing is fastest through the REPL, as you avoid environment startup
time.

```clojure
user=> (test)
...
```

But you can also run tests through Leiningen.

```sh
lein test
```

### Generators

This project has several [generators][] to help you create files.

* `lein gen endpoint <name>` to create a new endpoint
* `lein gen component <name>` to create a new component

[generators]: https://github.com/weavejester/lein-generate

## Deploying

FIXME: steps to deploy

## Legal

Copyright © 2016 FIXME
