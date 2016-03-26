FROM clojure:lein-2.5.3

COPY . /usr/src/app
WORKDIR /usr/src/app
CMD ["lein", "run"]
