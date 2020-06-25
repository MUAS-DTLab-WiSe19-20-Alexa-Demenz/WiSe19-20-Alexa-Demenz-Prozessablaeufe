package guided.procedures.handlers;

import com.amazon.ask.model.Response;
import guided.procedures.utils.HandlerMockUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SessionEndedRequestHandlerTest {
    private static SessionEndedRequestHandler handler;

    @BeforeClass
    public static void setup() {
        handler = new SessionEndedRequestHandler();
    }

    @Test
    public void testCanHandle() {
        assertTrue("Session Ended Handler should be able to handle SessionEndedRequest",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getSessionEndedRequest())));
    }

    @Test
    public void testCanNotHandleLaunchRequest() {
        assertFalse("Session Ended Handler should not be able to handle LaunchRequest",
                handler.canHandle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getLaunchRequest())));
    }

    @Test
    public void testHandle() {
        final Optional<Response> res = handler.handle(HandlerMockUtil.getHandlerWithRequest(HandlerMockUtil.getSessionEndedRequest()));
        assertTrue(res.isPresent());
        final Response response = res.get();
        assertTrue(response.getShouldEndSession());
    }
}
