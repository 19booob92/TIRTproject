package org.pwr.tirt.mod;

import java.util.List;

import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.dto.Subject;
import org.pwr.tirt.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class ScheduleComparator {

	@Autowired
	ScheduleService scheduleService;

	private ObjectMapper mapper = new ObjectMapper();

	public String compareTo(long firstIndexNo, long secondIndexNo)
			throws JsonProcessingException {
		List<Subject> firstScheduleSubjects = prepareSubjectLists(firstIndexNo);
		List<Subject> secondScheduleSubjects = prepareSubjectLists(secondIndexNo);

		firstScheduleSubjects.retainAll(secondScheduleSubjects);

		return mapper.writeValueAsString(firstScheduleSubjects);
	}

	private List<Subject> prepareSubjectLists(long indexNo) {
		ProcessedSchedule processedSchedule = scheduleService
				.findByIndexNo(indexNo);

		return processedSchedule.getSubject();
	}

}
