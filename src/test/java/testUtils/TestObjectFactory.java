package testUtils;

import org.pwr.tirt.model.SubjectDetails;
import org.pwr.tirt.model.dto.Subject;

public class TestObjectFactory {

	static public Subject prepareDefaultSchedule(String dayOfWeek, String startTime) {
		Subject subject = new Subject();
		
		subject.setDetails(prepareScheduleDetails(dayOfWeek, startTime));
		
		return subject;
	}
	
	static public SubjectDetails prepareScheduleDetails(String dayOfWeek, String startTime) {
		SubjectDetails scheduleDetails = new SubjectDetails();
		
		scheduleDetails.setDayOfWeek(dayOfWeek);
		scheduleDetails.setStart(startTime);
		
		return scheduleDetails;
	}
	
}
