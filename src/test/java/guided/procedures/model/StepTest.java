package guided.procedures.model;

import org.junit.Assert;
import org.junit.Test;

public class StepTest {

    //this test exists to improve the coverage
    @Test
    public void testStep() {
        int testNumber = 0;
        String testName = "testNameForStep";
        String testResponsePlainText = "testResponsePlainTextForStep";
        Long testConfirmationTimeInSeconds = null;

        Step step = new Step(testNumber, testName, testResponsePlainText, testConfirmationTimeInSeconds);
        //The assertions are completely pointless, step has no functionality that needs testing
        Assert.assertEquals("Step didn't return the correct number", testNumber, step.getStepNr());
        Assert.assertEquals("Step didn't return the correct name", testName, step.getName());
        Assert.assertEquals("Step didn't return the correct plain text", testResponsePlainText, step.getResponsePlainText());
        Assert.assertEquals("Step didn't return the correct confirmation time", testConfirmationTimeInSeconds, step.getConfirmationTime());
    }
}
