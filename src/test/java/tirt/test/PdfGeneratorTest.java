package tirt.test;

import java.io.File;
import java.io.IOException;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pwr.tirt.TirTprojectApplication;
import org.pwr.tirt.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.lowagie.text.DocumentException;

@WebAppConfiguration
@ContextConfiguration(classes = { TirTprojectApplication.class })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource({ "classpath:application.properties" })
public class PdfGeneratorTest {

    @Autowired
    ScheduleService defaulScheduleService;
    
    @Test
    public void shouldSaveProperPdfFile() throws IOException, DocumentException {
        int indexNo = 194225;
        File file = new File(this.getClass().getResource("/").getFile() + "/schedule" + indexNo + ".pdf");

        byte[] fileAsBytes = defaulScheduleService.generatePdf(indexNo);

        FileUtils.writeByteArrayToFile(file, fileAsBytes);
    }
}
