package org.pwr.tirt.mod;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pwr.tirt.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ConnectionProcessor {

    @Autowired
    StringUtils stringUtils;

    private StringBuilder content = new StringBuilder();
    private Document doc = null;

    public String fetchData() {

        try {
            //wchodzimy na jsos po ciastka
            Connection.Response getCookies = Jsoup.connect("https://jsos.pwr.edu.pl/")
                    .method(Connection.Method.GET)
                    .execute();
            
            //z tego co dostajemy pobieramy token i key

            Connection.Response loginForm = Jsoup.connect("https://jsos.pwr.edu.pl/index.php/site/loginAsStudent")
                    .method(Connection.Method.GET)
                    .cookies(getCookies.cookies())
                    .execute();

            String uriWithToken = loginForm.url().toString();
            
            System.err.println(uriWithToken);
            
            Document document = Jsoup.connect("http://oauth.pwr.edu.pl/oauth/authenticate")
                    .data("cookieexists", "false")
                    .data("oauth_symbol", "EIS")
                    .data("oauth_locale", "pl")
                    .data("oauth_token", stringUtils.getTokenFromUrl(uriWithToken))
                    .data("oauth_consumer_key", stringUtils.getConsumerKeyFromUrl(uriWithToken))
                    .data("username", "pwr194225")
                    .data("password", "marley1992bob")
                    .followRedirects(true)
                    .cookies(loginForm.cookies())
                    .post();

            // doc =
            // Jsoup.connect("https://jsos.pwr.edu.pl/index.php/student/zajecia").get();

            Elements newsHeadlines = document.select("div");

            for (Element el : newsHeadlines) {
                content.append(el.toString());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content.toString();
    }
}
