package org.pwr.tirt.controller;

import org.pwr.tirt.mod.ScheduleComparator;
import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.Schedule;
import org.pwr.tirt.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ScheduleController {

    @Autowired
    ScheduleComparator scheduleComparator;

    @Autowired
    ScheduleService scheduleService;
    
    @RequestMapping(value = "/schedule/save", method = RequestMethod.POST)
    public void saveSchedule(@RequestBody Schedule schedule) {
    	scheduleService.saveSchedule(schedule);
    }

    @RequestMapping(value = "/schedule/{indexNo}", method = RequestMethod.GET)
    public String fetchSchedule(@PathVariable long indexNo) {
        ProcessedSchedule schedule = scheduleService.findByIndexNo(indexNo);  
        return schedule.getSubjectsAsJson();
    }
    
    @RequestMapping(value = "/schedule/compare/{indexNo}/{indexNoToCompare}", method = RequestMethod.GET)
    public String compareByHoures(@PathVariable long indexNo, @PathVariable long indexNoToCompare) {
    	return scheduleComparator.compareTo(indexNo, indexNoToCompare);
    }
    
}
