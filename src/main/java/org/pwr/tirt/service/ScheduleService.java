package org.pwr.tirt.service;

import javax.transaction.Transactional;

import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.Schedule;
import org.springframework.stereotype.Service;

@Service
@Transactional
public interface ScheduleService {

	void saveSchedule(Schedule schedule);
	
	ProcessedSchedule findByIndexNo(long indexNo);
}
