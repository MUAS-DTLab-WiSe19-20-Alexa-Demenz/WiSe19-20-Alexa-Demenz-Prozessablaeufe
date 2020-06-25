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

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Log4j2
public class RepeatStepIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("RepeatStepIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        log.debug("handle() has been called");

        Intent intent = ((IntentRequest) handlerInput.getRequestEnvelope().getRequest()).getIntent();
        Map<String, Slot> slots = intent.getSlots();
        String speechText;
        SeverityLevel rate = parseRate(slots.get(PhrasesAndConstants.REPEAT_STEP_MODIFIER_SLOT).getValue());

        if (SessionAttributes.getProcedureManager() == null) {
            speechText = PhrasesAndConstants.NO_PROCEDURE_SELECTED;
        } else {
            speechText = SessionAttributes.getProcedureManager().getPreviousStepResponseText();
        }

        return handlerInput.getResponseBuilder()
                .withSimpleCard(PhrasesAndConstants.CARD_TITLE, PhrasesAndConstants.GENERAL_HELP)
                .withSpeech(ResponseTransformer.builder().setResponseText(speechText).applySpeechRate(rate).buildResponse())
                .withShouldEndSession(false)
                .build();
    }

    private SeverityLevel parseRate(String repeatModifier) {
        if (repeatModifier == null) {
            return SeverityLevel.MILD;
        } else {
            if (repeatModifier.equalsIgnoreCase("langsam") || repeatModifier.equalsIgnoreCase("langsamer")) {
                return SeverityLevel.MODERATE;
            } else {
                return SeverityLevel.SEVERE;
            }
        }
    }
}
