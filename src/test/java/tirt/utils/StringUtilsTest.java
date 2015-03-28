package tirt.utils;

import org.junit.Assert;
import org.junit.Test;
import org.pwr.tirt.model.SubjectDetails;
import org.pwr.tirt.utils.StringUtils;


public class StringUtilsTest {

    private StringUtils classUnderTest = new StringUtils();

    @Test
    public void shouldProperSplitGivenString() {
        String testString = "pt 13:15-15:00, bud. D-1, sala 312a";

        SubjectDetails details = classUnderTest.splitBuildingsAndTimeData(testString);

        Assert.assertEquals("pt", details.getDayOfWeek());
        Assert.assertEquals("13:15", details.getStart());
        Assert.assertEquals("15:00", details.getEnd());
        Assert.assertEquals("D-1", details.getBuilding());
        Assert.assertEquals("312a", details.getRoom());
    }
}
