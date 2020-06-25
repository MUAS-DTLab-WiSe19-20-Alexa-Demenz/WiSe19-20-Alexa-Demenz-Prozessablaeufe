package guided.procedures.model.util;

import guided.procedures.model.enumeration.SeverityLevel;
import org.junit.Assert;
import org.junit.Test;

public class ResponseTransformerTest {

    @Test
    public void testApplySpeechRateWithAllSeverityLevels() {
        String testString = "foo";

        String expectedOutputMild = "<prosody rate=\"medium\">" + testString + "</prosody>";
        String transformedOutputMild = ResponseTransformer.builder().setResponseText(testString)
                .applySpeechRate(SeverityLevel.MILD).buildResponse();
        Assert.assertEquals("The text isn't correctly formatted", expectedOutputMild, transformedOutputMild);

        String expectedOutputModerate = "<prosody rate=\"slow\">" + testString + "</prosody>";
        String transformedOutputModerate = ResponseTransformer.builder().setResponseText(testString)
                .applySpeechRate(SeverityLevel.MODERATE).buildResponse();
        Assert.assertEquals("The text isn't correctly formatted", expectedOutputModerate, transformedOutputModerate);

        String expectedOutputSevere = "<prosody rate=\"x-slow\">" + testString + "</prosody>";
        String transformedOutputSevere = ResponseTransformer.builder().setResponseText(testString)
                .applySpeechRate(SeverityLevel.SEVERE).buildResponse();
        Assert.assertEquals("The text isn't correctly formatted", expectedOutputSevere, transformedOutputSevere);
    }
}
