package org.pwr.tirt.service;

import javax.transaction.Transactional;

import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.ScheduleDto;
import org.springframework.stereotype.Service;

@Service
@Transactional
public interface ScheduleService {

	void saveSchedule(ScheduleDto schedule);
	
	ProcessedSchedule findByIndexNo(long indexNo);
}
