package guided.procedures.handlers;

import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import guided.procedures.SessionAttributes;
import guided.procedures.model.util.PhrasesAndConstants;
import guided.procedures.utils.HandlerMockUtil;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListProceduresIntentHandlerTest {
    private static ListProceduresIntentHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new ListProceduresIntentHandler();
    }

    @Test
    public void testCanNotHandleHelpIntent() {
        assertFalse("List Procedures Handler shouldn't be able to handle HelpIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("AMAZON.HelpIntent"))));
    }

    @Test
    public void testCanHandle() {
        assertTrue("List Procedures Handler couldn't handle ListProceduresIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("ListProceduresIntent"))));
    }

    @SneakyThrows
    @Test
    public void testHandleNoAvailableProcedures() {
        SessionAttributes.overrideResourceFile("testProcedures1.json");
        SessionAttributes.initiateAttributes();
        SessionAttributes.setAvailableProcedures(List.of());

        final Optional<Response> optionalResponse = handler.handle(HandlerMockUtil.getHandlerWithRequest(
                HandlerMockUtil.getIntentRequestWith("ListProceduresIntent", new HashMap<>() {{
                    put(PhrasesAndConstants.PROCEDURE_LIST_MODIFIER_SLOT, Slot.builder().build());
                }})));
        Assert.assertTrue(optionalResponse.isPresent());
        final Response response = optionalResponse.get();

        assertTrue(HandlerMockUtil.getSpeechTextFromResponseOutput(response.getOutputSpeech().toString())
                .contains(PhrasesAndConstants.NO_LOCAL_PROCEDURES_RESPONSE));
        assertFalse(response.getShouldEndSession());
        SessionAttributes.teardownAttributes();
    }

    @SneakyThrows
    @Test
    public void testHandleSomeAvailableProcedures() {
        SessionAttributes.overrideResourceFile("testProcedures1.json");
        SessionAttributes.initiateAttributes();
        SessionAttributes.setAvailableProcedures(List.of("Procedure one", "Procedure two"));

        final Optional<Response> optionalResponse = handler.handle(HandlerMockUtil.getHandlerWithRequest(
                HandlerMockUtil.getIntentRequestWith("ListProceduresIntent", new HashMap<>() {{
                    put(PhrasesAndConstants.PROCEDURE_LIST_MODIFIER_SLOT, Slot.builder().build());
                }})));
        Assert.assertTrue(optionalResponse.isPresent());
        final Response response = optionalResponse.get();

        assertTrue(HandlerMockUtil.getSpeechTextFromResponseOutput(response.getOutputSpeech().toString())
                .contains("Diese Vorgänge habe ich gefunden. Procedure one.  Procedure two. "));
        assertFalse(response.getShouldEndSession());
        SessionAttributes.teardownAttributes();
    }
}
