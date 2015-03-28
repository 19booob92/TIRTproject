package org.pwr.tirt.service.impl;

import org.pwr.tirt.mod.HtmlParser;
import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.ScheduleDto;
import org.pwr.tirt.repository.ScheduleRepository;
import org.pwr.tirt.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultScheduleService implements ScheduleService {

	@Autowired
	ScheduleRepository scheduleRepo;
	
	@Autowired
	HtmlParser htmlParser;
	
	@Override
	public void saveSchedule(ScheduleDto schedule) {
		ProcessedSchedule processedSchedule = scheduleRepo.save(new ProcessedSchedule());
		
		scheduleRepo.save(htmlParser.convertHtmlToProcessedSchedule(schedule, processedSchedule));
	}

	@Override
	public ProcessedSchedule findByIndexNo(long indexNo) {
		return scheduleRepo.findByIndexNo(indexNo).get(0);
	}
	
}
