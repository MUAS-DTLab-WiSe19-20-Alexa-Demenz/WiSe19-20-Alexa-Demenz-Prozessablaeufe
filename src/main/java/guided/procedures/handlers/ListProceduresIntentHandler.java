package guided.procedures.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import guided.procedures.SessionAttributes;
import guided.procedures.model.enumeration.SeverityLevel;
import guided.procedures.model.util.PhrasesAndConstants;
import guided.procedures.model.util.ResponseTransformer;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Log4j2
public class ListProceduresIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ListProceduresIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.debug("handle() has been called");

        Intent intent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        Map<String, Slot> slots = intent.getSlots();
        List<String> localProcedures = SessionAttributes.getAvailableProcedures();
        String speechText;
        SeverityLevel rate = SessionAttributes.getUserConfiguration().getSeverity();
        boolean listAllProcedures = parseListAllModifier(slots.get(PhrasesAndConstants.PROCEDURE_LIST_MODIFIER_SLOT).getValue());

        if (localProcedures.isEmpty()) {
            speechText = PhrasesAndConstants.NO_LOCAL_PROCEDURES_RESPONSE;
        } else {
            log.debug("Reading persistence attributes from DynamoDB");
            ArrayList<String> lastThreeProcedures = getLastThreeProcedures(input.getAttributesManager()
                    .getPersistentAttributes());

            StringBuilder stringBuilder = new StringBuilder().append("Diese Vorgänge habe ich gefunden.");
            if (listAllProcedures || lastThreeProcedures == null || lastThreeProcedures.size() < 3) {
                localProcedures.forEach(s -> stringBuilder.append(" ").append(s).append(". "));
            } else {
                lastThreeProcedures.forEach(s -> stringBuilder.append(" ").append(s).append(". "));
            }
            speechText = stringBuilder.toString();
        }

        return input.getResponseBuilder().withSimpleCard(PhrasesAndConstants.CARD_TITLE, speechText)
                .withSpeech(ResponseTransformer.builder().setResponseText(speechText).applySpeechRate(rate).buildResponse())
                .withShouldEndSession(false)
                .build();
    }

    private boolean parseListAllModifier(String listProceduresModifier) {
        return listProceduresModifier != null && listProceduresModifier.equals("alle");
    }

    private ArrayList<String> getLastThreeProcedures(Map<String, Object> persistenceAttributes) {
        @SuppressWarnings("unchecked")
        ArrayList<String> lastThreeProcedures = (ArrayList<String>) persistenceAttributes
                .get(PhrasesAndConstants.PERSISTENT_ATTRIBUTES_LAST_THREE_PROCEDURES);
        return lastThreeProcedures;
    }
}
