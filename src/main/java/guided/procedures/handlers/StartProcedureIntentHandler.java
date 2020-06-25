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

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.google.common.collect.Lists;
import guided.procedures.SessionAttributes;
import guided.procedures.model.ProcedureManager;
import guided.procedures.model.enumeration.SeverityLevel;
import guided.procedures.model.util.PhrasesAndConstants;
import guided.procedures.model.util.ResponseTransformer;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Log4j2
public class StartProcedureIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("StartProcedureIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.debug("handle() has been called");

        Intent intent = ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
        Map<String, Slot> slots = intent.getSlots();
        String requestedProcedure = slots.get(PhrasesAndConstants.PROCEDURE_SLOT).getValue();

        ProcedureManager procedureManager = new ProcedureManager().withProcedure(requestedProcedure);
        modifyLastThreeProceduresPersistenceAttributes(input.getAttributesManager(), requestedProcedure);

        SeverityLevel rate = SessionAttributes.getUserConfiguration().getSeverity();
        SessionAttributes.setProcedureManager(procedureManager);
        Long breakTime = procedureManager.getConfirmationTimeFromNextStep();
        String speechText = ResponseTransformer.builder().setResponseText(procedureManager.getNextStepResponseText())
                .applySpeechRate(rate).applySpeechAfterBreakTime("", breakTime).buildResponse();

        return input.getResponseBuilder().withSimpleCard(PhrasesAndConstants.CARD_TITLE, speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false).build();
    }

    private void modifyLastThreeProceduresPersistenceAttributes(AttributesManager attributesManager, String requestedProcedure) {
        log.debug("Reading persistence attributes from DynamoDB");
        Map<String, Object> persistenceAttributes = attributesManager.getPersistentAttributes();
        @SuppressWarnings("unchecked")
        ArrayList<String> lastThreeProcedures = (ArrayList<String>) persistenceAttributes
                .get(PhrasesAndConstants.PERSISTENT_ATTRIBUTES_LAST_THREE_PROCEDURES);

        if (lastThreeProcedures != null) {
            if (lastThreeProcedures.contains(requestedProcedure)) {
                lastThreeProcedures.remove(requestedProcedure);
                lastThreeProcedures.add(0, requestedProcedure);
            } else {
                lastThreeProcedures.add(0, requestedProcedure);
                if (lastThreeProcedures.size() == 4) {
                    lastThreeProcedures.remove(3);
                }
            }
        } else {
            lastThreeProcedures = Lists.newArrayList(requestedProcedure);
        }
        persistenceAttributes.put(PhrasesAndConstants.PERSISTENT_ATTRIBUTES_LAST_THREE_PROCEDURES, lastThreeProcedures);
        attributesManager.setPersistentAttributes(persistenceAttributes);
        log.debug("Writing persistence attributes to DynamoDB");
        attributesManager.savePersistentAttributes();
    }
}
