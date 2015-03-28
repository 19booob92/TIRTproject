package org.pwr.tirt.service.impl;

import java.io.IOException;

import org.pwr.tirt.mod.HtmlParser;
import org.pwr.tirt.mod.PdfGenerator;
import org.pwr.tirt.mod.TemplateProcessor;
import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.ScheduleDto;
import org.pwr.tirt.repository.ScheduleRepository;
import org.pwr.tirt.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

@Service
public class DefaultScheduleService implements ScheduleService {

	private static final String SCHEDULE_TEMPLATE_PATH = "templates/scheduleTemplate";
	
	@Autowired
	ScheduleRepository scheduleRepo;
	
	@Autowired
	PdfGenerator pdfGenerator;
	
	@Autowired
	TemplateProcessor templateProcessor;
	
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

	@Override
	public byte[] generatePdf(long indexNo) throws IOException, DocumentException {
		ProcessedSchedule processedSchedule = findByIndexNo(indexNo);
		
		String processedHtml = templateProcessor.generateHtmlString(SCHEDULE_TEMPLATE_PATH, processedSchedule);
		
		byte [] fileAsBytes = pdfGenerator.createPdf(processedHtml);
		
		return fileAsBytes;
	}
	
}
