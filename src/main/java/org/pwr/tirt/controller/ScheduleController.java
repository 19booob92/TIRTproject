package org.pwr.tirt.controller;

import java.util.List;

import org.pwr.tirt.mod.ScheduleComparator;
import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.ScheduleDto;
import org.pwr.tirt.model.dto.Subject;
import org.pwr.tirt.service.ScheduleService;
import org.pwr.tirt.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;


@RestController
public class ScheduleController {

    @Autowired
    ScheduleComparator scheduleComparator;

    @Autowired
    ScheduleService scheduleService;
    
    @RequestMapping(value = "/schedule/save", method = RequestMethod.POST)
    public void saveSchedule(@RequestBody ScheduleDto schedule) {
    	scheduleService.saveSchedule(schedule);
    }

    @RequestMapping(value = "/schedule/{indexNo}", method = RequestMethod.GET)
    public String fetchSchedule(@PathVariable long indexNo) throws JsonProcessingException {
        ProcessedSchedule schedule = scheduleService.findByIndexNo(indexNo); 
        return JsonUtils.getSubjectsAsJson(schedule);
    }
    
    @RequestMapping(value = "/schedule/compare/{indexNo}/{indexNoToCompare}", method = RequestMethod.GET)
    public String compareByHoures(@PathVariable long indexNo, @PathVariable long indexNoToCompare) {
    	return scheduleComparator.compareTo(indexNo, indexNoToCompare);
    }

    @RequestMapping(value = "/schedule/pdf/{indexNo}", method = RequestMethod.GET)
    public byte[] generatePdfFile(@PathVariable long indexNo) {
    	return null;
    }
    
}
