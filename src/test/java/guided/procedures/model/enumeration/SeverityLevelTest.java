package guided.procedures.model.enumeration;

import org.junit.Assert;
import org.junit.Test;

public class SeverityLevelTest {

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf() {
        Assert.assertEquals(SeverityLevel.MILD, SeverityLevel.valueOf(1));
        Assert.assertEquals(SeverityLevel.MODERATE, SeverityLevel.valueOf(2));
        Assert.assertEquals(SeverityLevel.SEVERE, SeverityLevel.valueOf(3));
        @SuppressWarnings("unused")
        SeverityLevel ignored = SeverityLevel.valueOf(42);
    }
}
