# Example that reproduces [OpenJ9 0.11.0 SSL's `bad_record_mac` issue](https://github.com/eclipse/openj9/issues/3637)

1. Run `docker run -d -p 9443:443 -e SERVER_NAME=foobar.example.com -v /my_self_signed.crt:/usr/local/apache2/conf/server.crt -v /my_self_signed.key:/usr/local/apache2/conf/server.key ilkka/httpd-ssl` (replace `/my_self_signed.crt` and `/my_self_signed.key` with your self-signed certificate file and key full paths)
1. `./gradlew run` with a `JAVA_HOME` pointing to OpenJDK will work just fine, with OpenJ9 it will crash (`javax.net.ssl.SSLException: Received fatal alert: bad_record_mac`)

Running with `-Djavax.net.debug=ssl:handshake:verbose` reveals that the chosen cipher is `TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384`.