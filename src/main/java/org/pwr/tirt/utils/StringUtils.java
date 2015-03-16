package org.pwr.tirt.utils;

import org.apache.activemq.filter.function.splitFunction;
import org.pwr.tirt.model.ScheduleDetails;
import org.springframework.stereotype.Component;


@Component
public class StringUtils {

    private static final int DAY_OF_WEEK_POSITION = 0;
    private static final int START_DATE_POSITION = 0;
    private static final int END_DATE_POSITION = 1;
    private static final int BUILDING_POSITION = 3;
    private static final int ROOM_POSITION = 5;
    
    private String tokenStartMarker = "oauth_token=";
    private String keyStartMarker = "&oauth_consumer_key=";
    private String localeStartMarker = "&oauth_locale";

    public String getTokenFromUrl(String url) {

        return url.substring(url.indexOf(tokenStartMarker) + tokenStartMarker.length(), url.indexOf(keyStartMarker));
    }

    public String getConsumerKeyFromUrl(String url) {

        return url.substring(url.indexOf(keyStartMarker) + keyStartMarker.length(), url.indexOf(localeStartMarker));
    }

    public ScheduleDetails splitBuildingsAndTimeData(String input) {
        //example input "pt 13:15-15:00, bud. D-1, sala 312a"

        ScheduleDetails details = new ScheduleDetails();
        
        String [] splitedString = input.split(" ");
        String [] splitedTime = splitedString[1].split("-");
        
        details.setDayOfWeek(splitedString[DAY_OF_WEEK_POSITION]);
        details.setStart(splitedTime[START_DATE_POSITION]);
        details.setEnd(splitedTime[END_DATE_POSITION].replace(',', ' ').trim());
        details.setBuilding(splitedString[BUILDING_POSITION].replace(',', ' ').trim());
        details.setRoom(splitedString[ROOM_POSITION]);
        
        return details;
    }
}
