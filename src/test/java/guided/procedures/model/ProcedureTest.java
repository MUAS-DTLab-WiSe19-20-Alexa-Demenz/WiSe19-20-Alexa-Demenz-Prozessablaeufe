package guided.procedures.model;

import com.google.common.collect.Lists;
import guided.procedures.SessionAttributes;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ProcedureTest {

    @Test
    public void buildProcedureWithThreeStepsTest() {
        List<Step> steps = Lists.newArrayList();
        Procedure procedure = new Procedure("test", steps);
        Step step1 = new Step(1, "first", "", null);
        Step step2 = new Step(2, "second", "", null);
        Step step3 = new Step(3, "third", "", null);
        procedure.addStep(step1);
        procedure.addStep(step2);
        procedure.addStep(step3);
        Assert.assertEquals("Step 1 was not retrieved correctly from procedure", step1, procedure.getStep(0));
        Assert.assertEquals("Step 2 was not retrieved correctly from procedure", step2, procedure.getStep(1));
        Assert.assertEquals("Step 3 was not retrieved correctly from procedure", step3, procedure.getStep(2));
    }

    @Test
    public void addStepToProcedureReturnsFalseForExistingStepNr() {
        List<Step> steps = Lists.newArrayList();
        Step step1 = new Step(1, "first", "", null);
        steps.add(step1);
        Procedure procedure = new Procedure("test", steps);
        Step step2 = new Step(1, "second", "", null);
        Assert.assertFalse("Adding step with same stepNr didn't return false", procedure.addStep(step2));
    }

    @Test
    public void addStepToProcedureReturnsFalseForExistingStepName() {
        List<Step> steps = Lists.newArrayList();
        Step step1 = new Step(1, "first", "", null);
        steps.add(step1);
        Procedure procedure = new Procedure("test", steps);
        Step step2 = new Step(2, "first", "", null);
        Assert.assertFalse("Adding step with same stepName didn't return false", procedure.addStep(step2));
    }

    @Test
    public void removeStepByNr() {
        List<Step> steps = Lists.newArrayList();
        Procedure procedure = new Procedure("test", steps);
        Step step1 = new Step(1, "first", "", null);
        procedure.addStep(step1);
        Assert.assertTrue("Removing an existing step should return true", procedure.removeStep(1));
    }

    @Test
    public void removeStepByName() {
        List<Step> steps = Lists.newArrayList();
        Procedure procedure = new Procedure("test", steps);
        Step step1 = new Step(1, "first", "", null);
        procedure.addStep(step1);
        Assert.assertTrue("Removing an existing step should return true", procedure.removeStep("first"));
    }

    @Test
    public void removeNonExistingStep() {
        List<Step> steps = Lists.newArrayList();
        Procedure procedure = new Procedure("test", steps);
        Assert.assertFalse("Removing a non-existing step should return false", procedure.removeStep(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseProcedure1FailWith_testProcedures2json_Test() {
        //TODO: use constructor of procedure
        try {
            SessionAttributes.overrideResourceFile("testProcedures2.json");
            SessionAttributes.initiateAttributes();
            new ProcedureManager().withProcedure("test1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseProcedure2FailWith_testProcedures2json_Test() {
        //TODO: use constructor of procedure
        try {
            SessionAttributes.overrideResourceFile("testProcedures2.json");
            SessionAttributes.initiateAttributes();
            new ProcedureManager().withProcedure("test2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAmountOfSteps() {
        Procedure procedure = new Procedure("", List.of());
        Assert.assertEquals(0, procedure.getAmountOfSteps());
    }
}
