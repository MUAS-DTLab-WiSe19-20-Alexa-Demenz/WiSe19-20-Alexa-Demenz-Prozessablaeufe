package guided.procedures.handlers;

import com.amazon.ask.model.Response;
import guided.procedures.SessionAttributes;
import guided.procedures.utils.HandlerMockUtil;
import guided.procedures.utils.Initializator;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class RepeatStepIntentHandlerTest {
    private static RepeatStepIntentHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new RepeatStepIntentHandler();
    }

    @After
    public void destroy(){
        SessionAttributes.teardownAttributes();
    }

    @Test
    public void testCanHandleRepeatStepIntent() {
        assertTrue("Repeat Step Handler wasn't able to handle FallbackIntent",
                handler.canHandle(HandlerMockUtil
                        .getHandlerWithRequest(HandlerMockUtil
                                .getIntentRequestWith("RepeatStepIntent"))));
    }

    @Test
    public void testCanNotHandleHelpIntent() {
        assertFalse("Repeat Step Handler shouldn't be able to handle HelpIntent",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getIntentRequestWith("AMAZON.NextIntent"))));
    }

    @Test
    public void testRepeatStepSuccessfully() {
        Initializator init = Initializator.builder()
                .withFileName("testProcedures1.json")
                .withHandler(handler)
                .withProcedure("test1")
                .withProcedureManager()
                .withRequest(HandlerMockUtil.getRepeatStepIntentRequest())
                .build();
        init.getProcedureManager().getNextStepResponseText();

        Optional<Response> response = init.getUpdatedResponse();
        assertTrue("response is not present", response.isPresent());
        assertNotNull("Response is empty", response.get());
        assertTrue(response.get().toString().contains("Mache den ersten Schritt"));
        SessionAttributes.teardownAttributes();
    }

    @Test
    public void testRepeatStepWhenNoProcedureIsSelected() {
        Initializator init = Initializator.builder()
                .withFileName("testProcedures1.json")
                .withHandler(handler)
                .withRequest(HandlerMockUtil.getRepeatStepIntentRequest()).build();

        Optional<Response> response = init.getUpdatedResponse();
        assertTrue("response is not present", response.isPresent());
        assertNotNull("Response is empty", response.get());
        assertTrue("does not contain correct response", response.get().toString().contains("Starte zuerst einen Vorgang"));
        SessionAttributes.teardownAttributes();
    }
}
