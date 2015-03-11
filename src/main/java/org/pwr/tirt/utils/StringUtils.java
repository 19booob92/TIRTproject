package org.pwr.tirt.utils;

import org.springframework.stereotype.Component;


@Component
public class StringUtils {

    private String tokenStartMarker = "oauth_token=";
    private String keyStartMarker = "&oauth_consumer_key=";
    private String localeStartMarker = "&oauth_locale";

    public String getTokenFromUrl(String url) {

        return url.substring(url.indexOf(tokenStartMarker) + tokenStartMarker.length(), url.indexOf(keyStartMarker));
    }

    public String getConsumerKeyFromUrl(String url) {

        return url.substring(url.indexOf(keyStartMarker) + keyStartMarker.length(), url.indexOf(localeStartMarker));
    }


}
