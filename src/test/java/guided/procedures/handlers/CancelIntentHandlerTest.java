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

public class CancelIntentHandlerTest {
    private static CancelIntentHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new CancelIntentHandler();
    }

    @Test
    public void testCanNotHandleHelpIntent() {
        assertFalse("Cancel Handler shouldn't be able to handle HelpIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("AMAZON.HelpIntent"))));
    }

    @Test
    public void testCanHandle() {
        assertTrue("Cancel Intent couldn't handle CancelIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("CancelIntent"))));
    }

    @SneakyThrows
    @Test
    public void testHandleOutsideOfAProcedure() {
        SessionAttributes.overrideResourceFile("testProcedures1.json");
        SessionAttributes.initiateAttributes();

        final Optional<Response> optionalResponse = handler.handle(HandlerMockUtil.getHandlerWithRequest(
                HandlerMockUtil.getIntentRequestWith("CancelIntent")));
        assertTrue("Cancel Handler should return a Response", optionalResponse.isPresent());

        String outputSpeech = optionalResponse.get().getOutputSpeech().toString();
        assertTrue("End Skill text wasn't in the output text section",
                HandlerMockUtil.getSpeechTextFromResponseOutput(outputSpeech).contains(PhrasesAndConstants.END_SKILL));
        assertTrue("End Session tag should be set to true", optionalResponse.get().getShouldEndSession());
        SessionAttributes.teardownAttributes();
    }

    @SneakyThrows
    @Test
    public void testHandleInsideOfAProcedure() {
        SessionAttributes.overrideResourceFile("testProcedures1.json");
        SessionAttributes.initiateAttributes();
        SessionAttributes.setProcedureManager(new ProcedureManager().withProcedure("test1"));

        final Optional<Response> optionalResponse = handler.handle(HandlerMockUtil.getHandlerWithRequest(
                HandlerMockUtil.getIntentRequestWith("CancelIntent")));
        assertTrue("Cancel Handler should return a Response", optionalResponse.isPresent());

        String outputSpeech = optionalResponse.get().getOutputSpeech().toString();
        assertTrue("Cancel Procedure text wasn't in the output text section",
                HandlerMockUtil.getSpeechTextFromResponseOutput(outputSpeech).contains(PhrasesAndConstants.CANCEL_PROCEDURE));
        assertFalse("End Session tag should be false when canceling a procedure", optionalResponse.get().getShouldEndSession());
        SessionAttributes.teardownAttributes();
    }
}
