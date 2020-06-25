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
public class FallbackIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.FallbackIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.debug("handle() has been called");
        SeverityLevel rate = SessionAttributes.getUserConfiguration().getSeverity();
        return input.getResponseBuilder()
                .withSpeech(ResponseTransformer.builder().setResponseText(PhrasesAndConstants.FALLBACK)
                        .applySpeechRate(rate).buildResponse())
                .withSimpleCard(PhrasesAndConstants.CARD_TITLE, PhrasesAndConstants.FALLBACK)
                .withReprompt(PhrasesAndConstants.FALLBACK)
                .build();
    }
}
