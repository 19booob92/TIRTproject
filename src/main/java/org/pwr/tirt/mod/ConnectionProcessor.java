package org.pwr.tirt.mod;


import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.pwr.tirt.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ConnectionProcessor {

    @Autowired
    StringUtils stringUtils;

    private StringBuilder content = new StringBuilder();
    private Document doc = null;

    public static void main(String[] args) {
        fetchData();
    }
    
    static public String fetchData() {

        try {
            Connection connection = Jsoup.connect("https://edukacja.pwr.wroc.pl/EdukacjaWeb/start.do");

            //wchodzimy na edu po ciastka
            // i po dokument
            Connection.Response rsp = connection.method(Connection.Method.GET).execute(); 
            Document document = connection.get();
            
//            Element sessionTOKEN = document.select("input[name=clEduWebSESSIONTOKEN]").get(0);
            Element webTOKEN = document.select("input[name=cl.edu.web.TOKEN]").get(0);
            
            Map<String, String> cookies = rsp.cookies();
            
            
            System.err.println(cookies.toString());
            
            
            Map<String, String> data = new HashMap<>();
            
            data.put("cl.edu.web.TOKEN", webTOKEN.val());
            data.put("login", "pwr158304");
            data.put("password", "bob1992marley");
            //z tego co dostajemy pobieramy token i key
            
            Document doc = Jsoup.connect("https://edukacja.pwr.wroc.pl/EdukacjaWeb/logInUser.do")
                    .method(Connection.Method.POST)
                    .cookies(cookies)
                    .data(data)
                    .followRedirects(true)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Content-Length", "128")
                    .post();
            
            System.err.println(doc.toString());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        return content.toString();
        return null;
    }
}
