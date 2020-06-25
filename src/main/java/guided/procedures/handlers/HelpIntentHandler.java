/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package guided.procedures.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import guided.procedures.SessionAttributes;
import guided.procedures.model.ProcedureManager;
import guided.procedures.model.enumeration.SeverityLevel;
import guided.procedures.model.util.PhrasesAndConstants;
import guided.procedures.model.util.ResponseTransformer;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Log4j2
public class HelpIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.debug("handle() has been called");

        ProcedureManager activeProcedureManager = SessionAttributes.getProcedureManager();
        SeverityLevel rate = SessionAttributes.getUserConfiguration().getSeverity();
        String speechText;

        if (aProcedureHasBeenBuilt(activeProcedureManager)) {
            speechText = "Jetzt ist der Vorgang " + activeProcedureManager.getBuiltProcedure().getName() + " aktiv. "
                    + PhrasesAndConstants.ACTIVE_PROCEDURE_HELP;
        } else {
            speechText = PhrasesAndConstants.GENERAL_HELP;
        }

        return input.getResponseBuilder()
                .withSimpleCard(PhrasesAndConstants.CARD_TITLE, PhrasesAndConstants.GENERAL_HELP)
                .withSpeech(ResponseTransformer.builder().setResponseText(speechText).applySpeechRate(rate).buildResponse())
                .withReprompt(PhrasesAndConstants.HELP_REPROMPT)
                .withShouldEndSession(false)
                .build();
    }

    private boolean aProcedureHasBeenBuilt(ProcedureManager activeProcedureManager) {
        return activeProcedureManager != null && activeProcedureManager.getBuiltProcedure() != null;
    }
}
