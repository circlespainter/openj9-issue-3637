import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

// RUN FIRST: `docker run -d -p 9443:443 -e SERVER_NAME=foobar.example.com -v /my_self_signed.crt:/usr/local/apache2/conf/server.crt -v /my_self_signed.key:/usr/local/apache2/conf/server.key ilkka/httpd-ssl`

fun main(args: Array<String>) {
    val trustAnyTrustManager = object : X509TrustManager {
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) = Unit
        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) = Unit
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }

    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, arrayOf(trustAnyTrustManager), SecureRandom())

    val clientBuilder = OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, trustAnyTrustManager)
        .hostnameVerifier { _, _ -> true }

    val client = clientBuilder.build()

    val request = Request.Builder()
        .url("https://localhost:9443")
        .build()

    val response = client.newCall(request).execute()

    println(response.body()?.string())
}