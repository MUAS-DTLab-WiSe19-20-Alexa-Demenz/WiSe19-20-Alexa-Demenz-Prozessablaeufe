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
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import guided.procedures.SessionAttributes;
import guided.procedures.model.enumeration.SeverityLevel;
import guided.procedures.model.util.PhrasesAndConstants;
import guided.procedures.model.util.ResponseTransformer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

@Log4j2
public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.debug("handle() has been called");

        ResponseBuilder responseBuilder = input.getResponseBuilder();
        String speechText;
        try {
            SessionAttributes.initiateAttributes();
            SeverityLevel rate = SessionAttributes.getUserConfiguration().getSeverity();
            speechText = ResponseTransformer.builder().setResponseText(PhrasesAndConstants.WELCOME)
                    .applySpeechRate(rate).buildResponse();
        } catch (IOException e) {
            log.warn(e);
            speechText = PhrasesAndConstants.LAUNCH_ERROR;
        }

        return responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, PhrasesAndConstants.WELCOME)
                .withSpeech(speechText)
                .withReprompt(PhrasesAndConstants.HELP_REPROMPT).build();
    }
}
