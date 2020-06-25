package guided.procedures.model.enumeration;

//Describes the progression of the patients disease
public enum SeverityLevel {
    MILD, MODERATE, SEVERE;

    public static SeverityLevel valueOf(int level) {
        switch (level) {
            case 1:
                return SeverityLevel.MILD;
            case 2:
                return SeverityLevel.MODERATE;
            case 3:
                return SeverityLevel.SEVERE;
            default:
                throw new IllegalArgumentException("Illegal argument: \"" + level + "\", Severity Level range from 1 to 3");
        }
    }
}
