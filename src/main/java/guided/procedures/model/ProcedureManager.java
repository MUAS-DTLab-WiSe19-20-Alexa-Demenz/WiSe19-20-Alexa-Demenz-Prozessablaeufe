package guided.procedures.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import guided.procedures.SessionAttributes;
import guided.procedures.model.util.JsonFieldNames;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;

@Log4j2
public class ProcedureManager {

    @Getter
    private Procedure builtProcedure;
    @Getter
    private int currentStepNr = 0;

    public ProcedureManager withProcedure(String procedureName) {
        List<JsonNode> availableProcedures = Lists.newArrayList();

        //add all procedure names to available procedures
        SessionAttributes.getJsonNode().get(JsonFieldNames.USER_DATA_FIELD_NAME).get(JsonFieldNames.PROCEDURES_FIELD_NAME).elements()
                .forEachRemaining(availableProcedures::add);

        //get the procedure with the given name or throw an exception
        JsonNode procedureDefinition = availableProcedures.stream().filter(jsonNode ->
                jsonNode.get(JsonFieldNames.PROCEDURE_NAME_FIELD_NAME).asText().equalsIgnoreCase(procedureName))
                .findAny().orElseThrow();

        List<Step> procedureSteps = Lists.newArrayList();

        //build all the steps for the procedure from the json input
        procedureDefinition.get(JsonFieldNames.STEPS_FIELD_NAME).elements()
                .forEachRemaining(jsonNode -> {
                    try {
                        procedureSteps.add(buildStep(jsonNode));
                    } catch (IOException e) {
                        log.warn(e);
                    }
                });

        log.debug("Built new procedure \"" + procedureName + "\" with " + procedureSteps.size() + " steps");
        builtProcedure = new Procedure(procedureDefinition.get(JsonFieldNames.PROCEDURE_NAME_FIELD_NAME).textValue(), procedureSteps);
        builtProcedure.addStep(getFinalStepWithFarewellMessage(procedureSteps.size() + 1));
        return this;
    }

    private Step buildStep(JsonNode parsedJson) throws IOException {
        return new ObjectMapper().readValue(parsedJson.toString(), Step.class);
    }

    public Long getConfirmationTimeFromNextStep() {
        return builtProcedure.getStep(currentStepNr).getConfirmationTime();
    }

    public String getNextStepResponseText() {
        log.debug("Get next Step Text with the index: " + currentStepNr);
        return builtProcedure.getStep(currentStepNr++).getResponsePlainText();
    }

    public String getPreviousStepResponseText() {
        log.debug("Get the previous Step Text with the index: " + (currentStepNr - 1));
        return builtProcedure.getStep(currentStepNr - 1).getResponsePlainText();
    }

    public boolean isCurrentStepLast() {
        return (builtProcedure.getAmountOfSteps() - 1) == currentStepNr;
    }

    private Step getFinalStepWithFarewellMessage(int finalStepNr) {
        return new Step(finalStepNr, "FarewellResponseStep",
                "<say-as interpret-as=\"interjection\">bis bald.</say-as>.", null);
    }
}
