package tirt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pwr.tirt.TirTprojectApplication;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TirTprojectApplication.class)
@WebAppConfiguration
public class TirTprojectApplicationTests {

	@Test
	public void contextLoads() {
	}

}
