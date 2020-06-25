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

public class HelpIntentHandlerTest {
    private static HelpIntentHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new HelpIntentHandler();
    }

    @Test
    public void testCanHandle() {
        assertTrue("Help Handler couldn't handle HelpIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("AMAZON.HelpIntent"))));
    }

    @Test
    public void testCanNotHandleNextIntent() {
        assertFalse("Help Handler shouldn't be able to handle NextIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("AMAZON.NextIntent"))));
    }

    @SneakyThrows
    @Test
    public void testHandle() {
        SessionAttributes.overrideResourceFile("testProcedures1.json");
        SessionAttributes.initiateAttributes();

        final Optional<Response> optionalResponse = handler.handle(HandlerMockUtil.getHandlerWithRequest(
                HandlerMockUtil.getIntentRequest()));
        assertTrue("Help Handler should return a Response", optionalResponse.isPresent());

        String outputSpeech = optionalResponse.get().getOutputSpeech().toString();
        assertTrue("Help text wasn't in the output text section",
                HandlerMockUtil.getSpeechTextFromResponseOutput(outputSpeech).contains(PhrasesAndConstants.GENERAL_HELP));
        SessionAttributes.teardownAttributes();
    }
}
