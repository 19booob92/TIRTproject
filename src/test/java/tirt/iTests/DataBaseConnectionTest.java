package tirt.iTests;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pwr.tirt.TirTprojectApplication;
import org.pwr.tirt.model.ProcessedSchedule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import testUtils.TestObjectFactory;


@WebAppConfiguration
@ContextConfiguration(classes = { TirTprojectApplication.class })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class DataBaseConnectionTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void shouldLoadContext() {
    }

    @Test
    public void shouldSaveNewScheduleInDataBase() {
        ProcessedSchedule processedSchedule = TestObjectFactory.prepareDefaultProcessedSchedule();

        Assert.assertTrue(em.isOpen());

        em.persist(processedSchedule);
        em.flush();
    }

}
