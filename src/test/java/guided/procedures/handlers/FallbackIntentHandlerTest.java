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

public class FallbackIntentHandlerTest {
    private static FallbackIntentHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new FallbackIntentHandler();
    }

    @Test
    public void testCanHandleFallBackIntent() {
        assertTrue("Fallback Handler wasn't able to handle FallbackIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("AMAZON.FallbackIntent"))));
    }

    @Test
    public void testCanNotHandleNextIntent() {
        assertFalse("Fallback Handler shouldn't be able to handle NextIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("NextIntent"))));
    }

    @SneakyThrows
    @Test
    public void testHandle() {
        SessionAttributes.overrideResourceFile("testProcedures1.json");
        SessionAttributes.initiateAttributes();

        final Optional<Response> optionalResponse = handler.handle(HandlerMockUtil.getHandlerWithRequest(
                HandlerMockUtil.getIntentRequest()));
        assertTrue("Fallback Handler should return a Response", optionalResponse.isPresent());

        String outputSpeech = optionalResponse.get().getOutputSpeech().toString();
        assertTrue("Fallback text wasn't in the output text section",
                HandlerMockUtil.getSpeechTextFromResponseOutput(outputSpeech).contains(PhrasesAndConstants.FALLBACK));
        SessionAttributes.teardownAttributes();
    }
}
