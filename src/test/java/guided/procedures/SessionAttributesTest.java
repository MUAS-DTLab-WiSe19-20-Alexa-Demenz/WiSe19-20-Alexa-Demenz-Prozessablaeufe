package guided.procedures;

import guided.procedures.model.enumeration.SeverityLevel;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SessionAttributesTest {

    @Test
    public void parseProcedureWith_testProcedures1json_SeverityLevelTest() {
        try {
            SessionAttributes.overrideResourceFile("testProcedures1.json");
            SessionAttributes.initiateAttributes();
            Assert.assertEquals("Severity has not been parsed correctly", SeverityLevel.MILD,
                    SessionAttributes.getUserConfiguration().getSeverity());
            SessionAttributes.teardownAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
