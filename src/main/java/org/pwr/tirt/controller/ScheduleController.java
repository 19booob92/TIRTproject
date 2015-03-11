package org.pwr.tirt.controller;

import java.util.List;

import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.Schedule;
import org.pwr.tirt.repository.ScheduleRepository;
import org.pwr.tirt.service.HtmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;


@RestController
public class ScheduleController {

    @Autowired
    HtmlParser htmlParser;

    @Autowired
    ScheduleRepository scheduleRepository;

    @RequestMapping(value = "/schedule/save", method = RequestMethod.POST)
    public void saveSchedule(@ModelAttribute("schedule") Schedule schedule) {
        scheduleRepository.save(htmlParser.convertHtmlToProcessedSchedule(schedule));
    }

    @RequestMapping(value = "/schedule/{indexNo}", method = RequestMethod.GET, produces = "appliction/json")
    public String fetchSchedule(@PathVariable long indexNo) {
        List<ProcessedSchedule> schedule = scheduleRepository.findByIndexNo(indexNo);
        
        return schedule.get(0).getSubjectsAsJson();
    }
}
