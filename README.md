# Park

A web application supporting the sharing of parking spaces by
residents of a building.

In my setup, relies on Apache to terminate SSL using a
LetsEncrypt-managed certificate, for a subdomain that is reverse
proxied to a different port on localhost, so we only need to deal with
ordinary HTTP communication.

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed, and a recent Java
distribution, to build and run the Clojure web application.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run

For a deployed installation, you will want to build a standalone
überjar, using:

    lein uberjar

Then copy the resulting `target/uberjar/park.jar` to somewhere like
`/usr/local/lib/park.jar`, and configure it to be run as a system
daemon. Here is an example of a `systemd` configuration file which
does that on a modern Linux, `park.service`:

    # See https://github.com/brunchboy/park

    [Unit]
    Description=The Park Web Application
    Wants=httpd-init.service
    After=network.target remote-fs.target nss-lookup.target httpd-init.service

    [Service]
    Type=simple
    Environment="DATABASE_URL=jdbc:postgresql:park?user=park&password=...elided..." "WEBSOCKET_TOKEN=...elided..."  "OPENWEATHER_API_KEY=...elided..." "IFTTT_WEBHOOK_KEY=...elided..." "JAVA_TOOL_OPTIONS=-Xmx256m" "NREPL_PORT=7001"
    SuccessExitStatus=143
    ExecStart=/usr/bin/java -Dconf=/usr/local/etc/park.edn -jar /usr/local/lib/park.jar
    KillMode=process
    Restart=on-failure
    User=james
    Group=james

    [Install]
    WantedBy=multi-user.target

Because this file contains secrets such as passwords and tokens, you
should make sure it is readable only by root. And as you may have
noticed within it, there is a reference to a less-sensitive
configuration file that is read by the web server at startup. In my
case that is located at `/usr/local/etc/park.edn`, and has the
following contents:

```clojure
{:cdn-url  "https://d22vjwe5tlkwmh.cloudfront.net/park"}
```

This reflects the CloudFront content delivery network used to
efficiently serve images. You can skip using a CDN like that and embed
the images directly in the `img` subdirectory of the `resources`
folder, so they will be served by the web application directly. If you
do that, simply update the `:cdn-url` to point at that path within
your web application.


## Development

For development, you will want to bring up the application in a local
REPL for rapid testing of ideas. To facilitate that, you can put the
following content into the file `dev-config.edn` at the top level of
the project, and update it to reflect your setup. This is used only
for local development and is not committed to git.

As noted above, you can serve the images directly from inside your web
application rather than setting up a content distribution network to
serve them if you want to keep things simple.

```clojure
;; WARNING:
;;
;; The dev-config.edn file is used for local environment variables,
;; such as database credentials. This file is listed in .gitignore and
;; will be excluded from version control by Git.

{:dev        true
 :port       3000
 ;; when :nrepl-port is set the application starts the nREPL server on load
 :nrepl-port 7000

 ;; set your dev database connection URL here
 :database-url "jdbc:postgresql://localhost/park?user=park&password=...elided..."

 ;; Set the URL from which large images and other resources are served here
 :cdn-url "https://d22vjwe5tlkwmh.cloudfront.net/park"
 }
```

Once again, because this file contains secrets, you will want to make
sure it is readable only by your account.

## License

Released under the MIT license, http://opensource.org/licenses/MIT

Originally generated using Luminus version 4.38.

Copyright © 2023 James Elliott
