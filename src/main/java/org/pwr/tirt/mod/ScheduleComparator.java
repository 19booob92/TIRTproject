package org.pwr.tirt.mod;

import java.util.List;

import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.dto.Subject;
import org.pwr.tirt.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class ScheduleComparator {

	@Autowired
	ScheduleService scheduleService;

	private Gson gson = new Gson();

	public String compareTo(long firstIndexNo, long secondIndexNo) {
		List<Subject> firstScheduleSubjects = prepareSubjectLists(firstIndexNo);
		List<Subject> secondScheduleSubjects = prepareSubjectLists(secondIndexNo);

		firstScheduleSubjects.retainAll(secondScheduleSubjects);

		return gson.toJson(firstScheduleSubjects);
	}

	private List<Subject> prepareSubjectLists(long indexNo) {
		TypeToken<List<Subject>> typeToken = new TypeToken<List<Subject>>() {
		};

		ProcessedSchedule processedSchedule = scheduleService.findByIndexNo(
				indexNo);
		
//		String firstScheduleJson = processedSchedule.getSubjectsAsJson();

//		return gson.fromJson(firstScheduleJson, typeToken.getType());
		
		return null;
	}

	public String compareToOne2One(long indexNo, long indexNoToCompare) {
		// TODO Auto-generated method stub
		return null;
	}
}
