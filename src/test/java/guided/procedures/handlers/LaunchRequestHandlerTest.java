package guided.procedures.handlers;

import com.amazon.ask.model.Response;
import guided.procedures.SessionAttributes;
import guided.procedures.model.util.PhrasesAndConstants;
import guided.procedures.utils.HandlerMockUtil;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LaunchRequestHandlerTest {
    private static LaunchRequestHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new LaunchRequestHandler();
    }

    @Test
    public void testCanHandle() {
        assertTrue("Launch Handler couldn't handle LaunchRequest",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getLaunchRequest())));
    }

    @Test
    public void testCanNotHandleSessionEndedRequest() {
        assertFalse("Launch Handler shouldn't be able to handle SessionEndedRequest",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getSessionEndedRequest())));
    }

    @SneakyThrows
    @Test
    public void testHandle() {
        SessionAttributes.overrideResourceFile("testProcedures1.json");
        SessionAttributes.initiateAttributes();

        final Optional<Response> optionalResponse = handler.handle(HandlerMockUtil.getHandlerWithRequest(
                HandlerMockUtil.getIntentRequest()));
        assertTrue("Launch Handler should return a Response", optionalResponse.isPresent());

        assertTrue(HandlerMockUtil.getSpeechTextFromResponseOutput(optionalResponse.get().getOutputSpeech().toString())
                .contains(PhrasesAndConstants.CARD_TITLE));
        SessionAttributes.teardownAttributes();
    }
}
