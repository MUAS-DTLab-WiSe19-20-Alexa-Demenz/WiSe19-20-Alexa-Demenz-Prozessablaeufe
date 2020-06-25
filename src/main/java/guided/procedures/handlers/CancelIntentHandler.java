package guided.procedures.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import guided.procedures.SessionAttributes;
import guided.procedures.model.enumeration.SeverityLevel;
import guided.procedures.model.util.PhrasesAndConstants;
import guided.procedures.model.util.ResponseTransformer;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Log4j2
public class CancelIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("CancelIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        log.debug("handle() has been called");

        String speechText;
        boolean shouldEnd = false;
        if (SessionAttributes.getProcedureManager() == null) {
            speechText = PhrasesAndConstants.END_SKILL;
            shouldEnd = true;
        } else {
            SessionAttributes.setProcedureManager(null);
            SeverityLevel rate = SessionAttributes.getUserConfiguration().getSeverity();
            speechText = ResponseTransformer.builder().setResponseText(PhrasesAndConstants.CANCEL_PROCEDURE)
                    .applySpeechRate(rate).buildResponse();
        }

        return handlerInput.getResponseBuilder()
                .withSimpleCard(PhrasesAndConstants.CARD_TITLE, PhrasesAndConstants.GENERAL_HELP)
                .withSpeech(speechText)
                .withReprompt(PhrasesAndConstants.ACTIVE_PROCEDURE_HELP)
                .withShouldEndSession(shouldEnd)
                .build();
    }
}
