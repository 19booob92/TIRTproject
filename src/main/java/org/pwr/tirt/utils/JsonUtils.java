package org.pwr.tirt.utils;

import org.pwr.tirt.model.ProcessedSchedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	private static ObjectMapper mapper = new ObjectMapper();
	
	public static String getSubjectsAsJson(ProcessedSchedule schedule) throws JsonProcessingException {
		return mapper.writeValueAsString(schedule.getSubject());
	}
	
}
