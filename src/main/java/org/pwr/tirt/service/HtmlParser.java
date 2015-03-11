package org.pwr.tirt.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.Schedule;
import org.pwr.tirt.model.Subject;
import org.pwr.tirt.repository.ScheduleRepository;
import org.pwr.tirt.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;


@Component
public class HtmlParser {

    @Autowired
    SubjectRepository subjectRepo;
    
    private static final String TABLE_TAG_NAME = "hrefZapisaneGrupySluchaczaTabela6764821";
    private static final int TABLE_NUMBER = 17;
    private static final int FIRST_PROPER_TR = 4;
    
    private static final int NAME_TD_NUM = 2;
    private static final int LECTOR_TD_NUM = 0;
    private static final int CORSE_TYPE_TD_NUM = 1;
    
    private static final int JUMP = 4;
    
    private Gson gson = new Gson();
    
    ProcessedSchedule processedSchedule = new ProcessedSchedule();

    public ProcessedSchedule convertHtmlToProcessedSchedule(Schedule schedule) {
        List<Subject> subjects = new ArrayList<>();

        Document doc = Jsoup.parse(schedule.getHtml());

        Element table = doc.select("table").get(TABLE_NUMBER);

        // to get childrens of <tbody>
        Elements trsInProperTable = table.children().first().children();
        List<Element> properTrs = trsInProperTable.subList(FIRST_PROPER_TR, trsInProperTable.size());

        for (int i = 0; i < properTrs.size() - (JUMP - 1); i+= JUMP) {
            
            Subject subject = new Subject();

            Element trCourseName = properTrs.get(i);
            Elements tds = trCourseName.children(); 

            subject.setName(tds.get(NAME_TD_NUM).text());
            
            Element trLector = properTrs.get(i+2);
            tds = trLector.children(); 
            
            subject.setLector(tds.get(LECTOR_TD_NUM).text());
            subject.setType(tds.get(CORSE_TYPE_TD_NUM).text());

            Element trOtherData = properTrs.get(i+3);
            Element tableIntrOtherData = trOtherData.select("table").first();
            Element otherDataTd = tableIntrOtherData.select("td").first();
            
            subject.setPlace(otherDataTd.text());
            subjects.add(subject);
        }
        
        processedSchedule.setSubjectsAsJson(gson.toJson(subjects));
        processedSchedule.setIndexNo(schedule.getIndexNo());
        
        return processedSchedule;
    }

}
