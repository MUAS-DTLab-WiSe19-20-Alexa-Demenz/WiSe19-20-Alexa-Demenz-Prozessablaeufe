package guided.procedures.model;

import guided.procedures.model.enumeration.SeverityLevel;
import lombok.Getter;

public class UserConfiguration {

    @Getter
    private SeverityLevel severity;

    public UserConfiguration(SeverityLevel level) {
        this.severity = level;
    }
}
