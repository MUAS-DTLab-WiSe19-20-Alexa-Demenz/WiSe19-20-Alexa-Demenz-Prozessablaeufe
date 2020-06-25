package guided.procedures.utils;

import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HandlerMockUtil {

    public static HandlerInput getHandlerWithRequest(Request request) {
        return getHandlerWithRequest(request, null);
    }

    public static HandlerInput getHandlerWithRequest(Request request, Map<String, Object> persistentAttributes) {
        if (persistentAttributes == null) {
            persistentAttributes = new HashMap<>();
        }
        final RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withRequest(request).build();

        PersistenceAdapter mockPersistenceAdapter = mock(PersistenceAdapter.class);
        when(mockPersistenceAdapter.getAttributes(requestEnvelope)).thenReturn(Optional.of(persistentAttributes));
        return HandlerInput.builder().withRequestEnvelope(requestEnvelope).withPersistenceAdapter(mockPersistenceAdapter).build();
    }

    public static IntentRequest getIntentRequest() {
        return getIntentRequestWith("DummyIntentHandler", new HashMap<>());
    }

    public static IntentRequest getIntentRequestWith(String intentName) {
        return getIntentRequestWith(intentName, new HashMap<>());
    }

    public static IntentRequest getIntentRequestWith(String intentName, Map<String, Slot> intentSlots) {
        return IntentRequest.builder()
                .withRequestId("requestId")
                .withTimestamp(OffsetDateTime.now())
                .withIntent(Intent.builder()
                        .withName(intentName)
                        .withSlots(intentSlots)
                        .build())
                .build();
    }

    public static LaunchRequest getLaunchRequest() {
        return LaunchRequest.builder()
                .withRequestId("requestId")
                .withTimestamp(OffsetDateTime.now())
                .build();
    }

    public static SessionEndedRequest getSessionEndedRequest() {
        return SessionEndedRequest.builder()
                .withRequestId("requestId")
                .withTimestamp(OffsetDateTime.now())
                .build();
    }

    public static IntentRequest getRepeatStepIntentRequest(){
        HashMap<String,Slot> intentes = new HashMap<>();
        intentes.put("Repeat_Modifier",Slot.builder().withValue("langsam").build());
        return getIntentRequestWith("",intentes);
    }

    public static IntentRequest getStartProcedureIntentRequest(String procedure){
        HashMap<String,Slot> intentes = new HashMap<>();
        intentes.put("Procedure",Slot.builder().withValue(procedure).build());
        return getIntentRequestWith("",intentes);
    }

    public static String getSpeechTextFromResponseOutput(String rawOutput) {
        int startIndex = rawOutput.indexOf("ssml:");
        return rawOutput.substring(startIndex, rawOutput.indexOf("\n", startIndex));
    }
}
