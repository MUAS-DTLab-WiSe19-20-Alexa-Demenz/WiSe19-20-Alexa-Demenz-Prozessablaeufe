package guided.procedures;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guided.procedures.model.ProcedureManager;
import guided.procedures.model.UserConfiguration;
import guided.procedures.model.enumeration.SeverityLevel;
import guided.procedures.model.util.JsonFieldNames;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SessionAttributes {
    private SessionAttributes() {
    }

    @Getter
    @Setter
    private static ProcedureManager procedureManager;

    @Getter
    @Setter
    private static UserConfiguration userConfiguration;

    @Getter
    @Setter
    private static JsonNode jsonNode;

    @Getter
    @Setter
    private static List<String> availableProcedures = new ArrayList<>();

    private static String RESOURCE_FILE = "savedProcedures.json";

    public static void initiateAttributes() throws IOException {
        AttributesBuilder attributesBuilder = new AttributesBuilder();
        setJsonNode(attributesBuilder.parseJsonFileFromResources());
        setUserConfiguration(attributesBuilder.buildUserConfiguration());
        setAvailableProcedures(attributesBuilder.getAvailableProcedures());
    }

    public static void overrideResourceFile(String newResourceFile) {
        RESOURCE_FILE = newResourceFile;
    }

    public static void teardownAttributes() {
        setProcedureManager(null);
        setAvailableProcedures(null);
        setUserConfiguration(null);
        setJsonNode(null);
        overrideResourceFile(null);
    }

    private static class AttributesBuilder {
        private JsonNode parseJsonFileFromResources() throws IOException {
            return new ObjectMapper().readTree(Objects.requireNonNull(getClass().getClassLoader().getResource(RESOURCE_FILE)));
        }

        private UserConfiguration buildUserConfiguration() {
            return new UserConfiguration(getSeverityLevelFromJson());
        }

        private SeverityLevel getSeverityLevelFromJson() {
            return SeverityLevel.valueOf(getJsonNode()
                    .get(JsonFieldNames.USER_DATA_FIELD_NAME).get(JsonFieldNames.CONFIGURATION_FIELD_NAME).get(JsonFieldNames.SPEED_FIELD_NAME).asInt());
        }

        private List<String> getAvailableProcedures() {
            return new ArrayList<>(getJsonNode().get(JsonFieldNames.USER_DATA_FIELD_NAME).get(JsonFieldNames.PROCEDURES_FIELD_NAME)
                    .findValuesAsText(JsonFieldNames.PROCEDURE_NAME_FIELD_NAME));
        }
    }
}
