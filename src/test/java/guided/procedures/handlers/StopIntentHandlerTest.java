package guided.procedures.handlers;

import com.amazon.ask.model.Response;
import guided.procedures.SessionAttributes;
import guided.procedures.model.util.PhrasesAndConstants;
import guided.procedures.utils.HandlerMockUtil;
import guided.procedures.utils.Initializator;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class StopIntentHandlerTest {

    private static StopIntentHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new StopIntentHandler();
    }

    @Test
    public void testCanNotHandleCancelIntent() {
        assertFalse("Stop Handler should not be able to handle CancelIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(
                        HandlerMockUtil.getIntentRequestWith("AMAZON.CancelIntent"))));
    }

    @Test
    public void testCanHandleStopIntent() {
        assertTrue("Stop Handler should be able to handle StopIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(
                        HandlerMockUtil.getIntentRequestWith("AMAZON.StopIntent"))));
    }

    @Test
    public void testCanNotHandleNextIntent() {
        assertFalse("Stop Handler should not be able to handle HelpIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(
                        HandlerMockUtil.getIntentRequestWith("AMAZON.HelpIntent"))));
    }

    @SneakyThrows
    @Test
    public void testHandle() {
        Initializator init = Initializator.builder()
                .withFileName("testProcedures1.json")
                .withHandler(handler)
                .withRequest(HandlerMockUtil.getIntentRequest())
                .build();
        Optional<Response> response = init.getUpdatedResponse();

        assertTrue(response.isPresent());
        final Response res = response.get();
        assertTrue(res.getShouldEndSession());
        assertNotNull(res.getOutputSpeech());
        assertTrue(HandlerMockUtil.getSpeechTextFromResponseOutput(res.getOutputSpeech().toString())
                .contains(PhrasesAndConstants.END_SKILL));
        SessionAttributes.teardownAttributes();
    }
}
