package guided.procedures.model;

import com.google.common.collect.Lists;
import guided.procedures.SessionAttributes;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class ProcedureManagerTest {

    @BeforeClass
    public static void setUp() {
        try {
            SessionAttributes.overrideResourceFile("testProcedures1.json");
            SessionAttributes.initiateAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void teardown() {
        SessionAttributes.teardownAttributes();
    }

    @Test
    public void parseProcedureWith_testProcedures1json_Procedure1NameTest() {
        ProcedureManager procedureManager = new ProcedureManager().withProcedure("test1");
        Assert.assertEquals("Procedure name has not been parsed correctly", "test1", procedureManager.getBuiltProcedure().getName());
    }

    @Test
    public void parseProcedureWith_testProcedures1json_Procedure2NameTest() {
        ProcedureManager procedureManager = new ProcedureManager().withProcedure("test2");
        Assert.assertEquals("Procedure name has not been parsed correctly", "test2", procedureManager.getBuiltProcedure().getName());
    }

    @Test
    public void parseProcedureWith_testProcedures1json_Procedure1Step1NameTest() {
        ProcedureManager procedureManager = new ProcedureManager().withProcedure("test1");
        Assert.assertEquals("Step name has not been parsed correctly", "Schritt1", procedureManager.getBuiltProcedure().getStep(0).getName());
    }

    @Test
    public void parseProcedureWith_testProcedures1json_Procedure1Step2NameTest() {
        ProcedureManager procedureManager = new ProcedureManager().withProcedure("test1");
        Assert.assertEquals("Step name has not been parsed correctly", "Schritt2", procedureManager.getBuiltProcedure().getStep(1).getName());
    }

    @Test
    public void parseProcedureWith_testProcedures1json_Procedure2Step1NameTest() {
        ProcedureManager procedureManager = new ProcedureManager().withProcedure("test2");
        Assert.assertEquals("Step name has not been parsed correctly", "Schritt1", procedureManager.getBuiltProcedure().getStep(0).getName());
    }

    @Test
    public void parseProcedureWith_testProcedures1json_Procedure2Step7NameTest() {
        ProcedureManager procedureManager = new ProcedureManager().withProcedure("test2");
        Assert.assertEquals("Step name has not been parsed correctly", "Schritt7", procedureManager.getBuiltProcedure().getStep(6).getName());
    }

    @Test
    public void availableProceduresWith_testProcedures1jsonTest() {
        Assert.assertEquals("Available procedures have not been parsed correctly", SessionAttributes.getAvailableProcedures(),
                Lists.newArrayList("test1", "test2"));
    }

    @Test
    public void getNextStepFromProcedureWith_testProcedures1jsonTest() {
        ProcedureManager procedureManager = new ProcedureManager().withProcedure("test1");
        Assert.assertEquals("First Step response text is not correct", "Mache den ersten Schritt", procedureManager.getNextStepResponseText());
        Assert.assertEquals("Second Step response text is not correct", "Mache den zweiten Schritt", procedureManager.getNextStepResponseText());
    }

    @Test
    public void getPreviousStepFromProcedureWith_testProcedures1jsonTest() {
        ProcedureManager procedureManager = new ProcedureManager().withProcedure("test1");
        Assert.assertEquals("First Step response text is not correct", "Mache den ersten Schritt", procedureManager.getNextStepResponseText());
        Assert.assertEquals("First Step repeat response text is not correct", "Mache den ersten Schritt", procedureManager.getPreviousStepResponseText());
        Assert.assertEquals("First Step repeat response text is not correct", "Mache den ersten Schritt", procedureManager.getPreviousStepResponseText());
    }

    @Test
    public void testIsCurrentStepLast() {
        Assert.assertFalse(new ProcedureManager().withProcedure("test1").isCurrentStepLast());
    }
}
