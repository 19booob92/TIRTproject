package tirt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.pwr.tirt.mod.ScheduleComparator;
import org.pwr.tirt.model.ProcessedSchedule;
import org.pwr.tirt.model.Subject;
import org.pwr.tirt.repository.ScheduleRepository;

import testUtils.TestObjectFactory;

import com.google.gson.Gson;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleComparatorTest {

	@Mock
	ScheduleRepository scheduleRepository;

	@InjectMocks
	private ScheduleComparator classUnderTest = new ScheduleComparator();

	Gson gson = new Gson();

	@Before
	public void init() {
		ProcessedSchedule scheduleOne = new ProcessedSchedule();
		ProcessedSchedule scheduleTwo = new ProcessedSchedule();

		List<Subject> subjects = new ArrayList<>();
		List<Subject> subjectsToCompare = new ArrayList<>();

		subjects.add(TestObjectFactory.prepareDefaultSchedule("pn", "7:30"));
		subjects.add(TestObjectFactory.prepareDefaultSchedule("wt", "8:15"));
		subjects.add(TestObjectFactory.prepareDefaultSchedule("śr", "7:30"));

		subjectsToCompare.add(TestObjectFactory.prepareDefaultSchedule("pn",
				"7:30"));

		scheduleOne.setSubjectsAsJson(gson.toJson(subjects));
		scheduleTwo.setSubjectsAsJson(gson.toJson(subjectsToCompare));

		Mockito.when(scheduleRepository.findByIndexNo(12L)).thenReturn(
				Arrays.asList(scheduleOne));
		Mockito.when(scheduleRepository.findByIndexNo(11L)).thenReturn(
				Arrays.asList(scheduleTwo));
	}

	@Test
	public void shouldReturnProperJsonWhenSchedulesContainsSameElements() {
		Assert.assertEquals(
				"[{\"id\":0,\"start\":0.0,\"end\":0.0,\"day\":0,\"details\":{\"dayOfWeek\":\"pn\",\"start\":\"7:30\"}}]",
				classUnderTest.compareTo(12, 11));
	}
}
