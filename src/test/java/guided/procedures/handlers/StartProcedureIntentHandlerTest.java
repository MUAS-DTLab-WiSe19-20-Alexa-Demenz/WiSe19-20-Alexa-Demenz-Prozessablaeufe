package guided.procedures.handlers;

import com.amazon.ask.model.Response;
import guided.procedures.SessionAttributes;
import guided.procedures.utils.HandlerMockUtil;
import guided.procedures.utils.Initializator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StartProcedureIntentHandlerTest {
    private static StartProcedureIntentHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new StartProcedureIntentHandler();
    }

    @Test
    public void testCanHandle() {
        assertTrue("Start Procedure Handler should be able to handle StartProcedureIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(
                        HandlerMockUtil.getIntentRequestWith("StartProcedureIntent"))));
    }

    @Test
    public void testCanNotHandleNextIntent() {
        assertFalse("Start Procedure Handler should not be able to handle NextIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(
                        HandlerMockUtil.getIntentRequestWith("AMAZON.NextIntent"))));
    }

    @Test
    public void testHandleStartedProcedure(){
        Initializator init = Initializator.builder()
                .withFileName("testProcedures1.json")
                .withHandler(handler)
                .withProcedure("test1")
                .withProcedureManager()
                .withRequest(HandlerMockUtil.getStartProcedureIntentRequest("test1"))
                .build();

        String readStep = init.getProcedureManager().getNextStepResponseText();
        Optional<Response> response = init.getUpdatedResponse();
        assertTrue(response.isPresent());
        assertTrue("wrong step read", response.get().toString().contains(readStep));
        SessionAttributes.teardownAttributes();
    }
}
