package testUtils;

import java.util.ArrayList;
import java.util.List;

import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.SubjectDetails;
import org.pwr.tirt.model.dto.Subject;


public class TestObjectFactory {

    static public Subject prepareDefaultSubject(String dayOfWeek, String startTime) {
        Subject subject = new Subject();

        subject.setDetails(prepareScheduleDetails(dayOfWeek, startTime));
            
        return subject;
    }

    static public List<Subject> prepareDefaultSubjects(int amount) {
        List<Subject> subjects = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            subjects.add(prepareDefaultSubject("wt", "7:30"));
        }
        return subjects;
    }

    static public SubjectDetails prepareScheduleDetails(String dayOfWeek, String startTime) {
        SubjectDetails scheduleDetails = new SubjectDetails();

        scheduleDetails.setDayOfWeek(dayOfWeek);
        scheduleDetails.setStart(startTime);

        return scheduleDetails;
    }

    static public ProcessedSchedule prepareDefaultProcessedSchedule() {
        ProcessedSchedule processedSchedule = new ProcessedSchedule();
        List<Subject> subjects = prepareDefaultSubjects(5);
        
        for (Subject subject : subjects) {
            subject.setProcessedSchedule(processedSchedule);
        }
        

        processedSchedule.setIndexNo(194225L);
        processedSchedule.setSubject(subjects);
        
        return processedSchedule;
    }

}
