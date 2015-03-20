package testUtils;

import org.pwr.tirt.model.ScheduleDetails;
import org.pwr.tirt.model.Subject;

public class TestObjectFactory {

	static public Subject prepareDefaultSchedule(String dayOfWeek, String startTime) {
		Subject subject = new Subject();
		
		subject.setDetails(prepareScheduleDetails(dayOfWeek, startTime));
		
		return subject;
	}
	
	static public ScheduleDetails prepareScheduleDetails(String dayOfWeek, String startTime) {
		ScheduleDetails scheduleDetails = new ScheduleDetails();
		
		scheduleDetails.setDayOfWeek(dayOfWeek);
		scheduleDetails.setStart(startTime);
		
		return scheduleDetails;
	}
	
}
