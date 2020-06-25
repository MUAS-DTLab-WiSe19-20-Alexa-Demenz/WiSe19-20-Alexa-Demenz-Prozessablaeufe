package guided.procedures.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Step {
    private Integer stepId;
    //1-based nr
    private int stepNr;
    private String name;

    private String responsePlainText;
    private Long confirmationTime;

    @JsonCreator
    public Step(@JsonProperty("stepNr") int stepNr, @JsonProperty("stepName")String stepName,
                @JsonProperty("responseText")String responseText, @JsonProperty("confirmationTime")Long confirmationTime) {
        this.stepNr = stepNr;
        this.name = stepName;
        this.responsePlainText = responseText;
        this.confirmationTime = confirmationTime;
        this.stepId = createStepId(stepName, responseText, stepNr);
    }

    private int createStepId(String name, String response, int nr) {
        return (name + response + nr).hashCode();
    }
}
