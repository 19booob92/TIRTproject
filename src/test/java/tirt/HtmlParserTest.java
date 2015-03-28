package tirt;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pwr.tirt.mod.HtmlParser;
import org.pwr.tirt.model.ScheduleDto;


public class HtmlParserTest {

    private HtmlParser htmlParser = new HtmlParser();
    
    private static final String TABLE_TAG_NAME = "hrefZapisaneGrupySluchaczaTabela6764821";

    private static final int TABLE_NUMBER = 17;
    private static final int FIRST_PROPER_TR = 4;
    
    @Test
    public void shouldFindAnchorWhichIsTableTag() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/edukacjaHtml.txt");
        
        String content = IOUtils.toString(stream, "UTF-8");
        
        ScheduleDto schedule = new ScheduleDto();
        schedule.setHtml(content);
        
        Document doc = Jsoup.parse(schedule.getHtml());
        
        Element tableTag = doc.select("a[name=" + TABLE_TAG_NAME +"]").first();
        
        Assert.assertEquals("<a name=\"hrefZapisaneGrupySluchaczaTabela6764821\"> </a>", tableTag.toString());
    }

    @Test
    @Ignore
    public void shouldFindTablefterTableTag() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/edukacjaHtml.txt");
        
        String content = IOUtils.toString(stream, "UTF-8");
        
        ScheduleDto schedule = new ScheduleDto();
        schedule.setHtml(content);
        
        Document doc = Jsoup.parse(schedule.getHtml());
        
        Element table = doc.select("table").get(TABLE_NUMBER);
        
        // to get childrens of <tbody>
        Elements trsInProperTable = table.children().first().children();
        List<Element> properTrs = trsInProperTable.subList(FIRST_PROPER_TR, trsInProperTable.size()-1);
        
        
//        System.err.println(htmlParser.convertHtmlToProcessedSchedule(schedule));
    }
    
}
