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

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import guided.procedures.model.util.PhrasesAndConstants;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GuidedProceduresStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        log.debug("getSkill() has been called");
        return Skills.standard()
                .addRequestHandlers(
                        new StartProcedureIntentHandler(),
                        new LaunchRequestHandler(),
                        new StopIntentHandler(),
                        new SessionEndedRequestHandler(),
                        new HelpIntentHandler(),
                        new FallbackIntentHandler(),
                        new NextStepIntentHandler(),
                        new RepeatStepIntentHandler(),
                        new CancelIntentHandler(),
                        new ListProceduresIntentHandler())
                .withTableName(PhrasesAndConstants.DYNAMO_DB_PROCEDURE_DATA_TABLE_NAME)
                .withAutoCreateTable(true)
                .build();
    }

    public GuidedProceduresStreamHandler() {
        super(getSkill());
    }
}
