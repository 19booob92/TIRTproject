package org.pwr.tirt.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.ScheduleDto;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

@Service
public interface ScheduleService {

	void saveSchedule(ScheduleDto schedule);
	
	ProcessedSchedule findByIndexNo(long indexNo);

	byte[] generatePdf(long indexNo) throws IOException, DocumentException;
}
