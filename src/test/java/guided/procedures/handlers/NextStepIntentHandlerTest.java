package guided.procedures.handlers;

import com.amazon.ask.model.Response;
import guided.procedures.SessionAttributes;
import guided.procedures.model.ProcedureManager;
import guided.procedures.model.util.PhrasesAndConstants;
import guided.procedures.utils.HandlerMockUtil;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NextStepIntentHandlerTest {
    private static NextStepIntentHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new NextStepIntentHandler();
    }

    @Test
    public void testCanHandle() {
        assertTrue("Next Step Handler should be able to handle NextStepIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("NextStepIntent"))));
    }

    @Test
    public void testCanNotHandleHelpIntent() {
        assertFalse("Next Step Handler shouldn't be able to handle HelpIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("AMAZON.HelpIntent"))));
    }

    @SneakyThrows
    @Test
    public void testHandleOutsideOfAProcedure() {
        SessionAttributes.overrideResourceFile("testProcedures1.json");
        SessionAttributes.initiateAttributes();

        final Optional<Response> optionalResponse = handler.handle(HandlerMockUtil.getHandlerWithRequest(
                HandlerMockUtil.getIntentRequest()));
        assertTrue("Cancel Handler should return a Response", optionalResponse.isPresent());

        assertTrue(HandlerMockUtil.getSpeechTextFromResponseOutput(optionalResponse.get().getOutputSpeech().toString())
                .contains(PhrasesAndConstants.NO_PROCEDURE_SELECTED));
        SessionAttributes.teardownAttributes();
    }

    @SneakyThrows
    @Test
    public void testHandleInsideOfAProcedure() {
        SessionAttributes.overrideResourceFile("testProcedures1.json");
        SessionAttributes.initiateAttributes();
        SessionAttributes.setProcedureManager(new ProcedureManager().withProcedure("test1"));

        final Optional<Response> optionalResponse = handler.handle(HandlerMockUtil.getHandlerWithRequest(
                HandlerMockUtil.getIntentRequest()));
        assertTrue("Cancel Handler should return a Response", optionalResponse.isPresent());

        assertTrue(HandlerMockUtil.getSpeechTextFromResponseOutput(optionalResponse.get().getOutputSpeech().toString())
                .contains("Mache den ersten Schritt"));
        SessionAttributes.teardownAttributes();
    }
}
