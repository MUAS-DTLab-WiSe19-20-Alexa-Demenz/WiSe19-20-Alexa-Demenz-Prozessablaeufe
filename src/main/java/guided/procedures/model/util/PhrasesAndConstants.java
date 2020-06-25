package guided.procedures.model.util;

public class PhrasesAndConstants {

    private PhrasesAndConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CARD_TITLE = "Vorhaben";
    public static final String PROCEDURE_SLOT = "Procedure";
    public static final String PROCEDURE_LIST_MODIFIER_SLOT = "List_Modifier";
    public static final String REPEAT_STEP_MODIFIER_SLOT = "Repeat_Modifier";
    public static final String DYNAMO_DB_PROCEDURE_DATA_TABLE_NAME = "swe-Do-25-dev-proceduresData";
    public static final String PERSISTENT_ATTRIBUTES_LAST_THREE_PROCEDURES = "lastThreeProceduresList";
    public static final String ACTIVE_PROCEDURE_HELP = "Sage Wiederhole, um den vorherigen Schritt nochmal zu hören oder " +
            "Weiter, um mit dem nächsten Schritt weiterzumachen";
    public static final String CANCEL_PROCEDURE = "OK, ich habe den Vorgang beendet.";

    public static final String WELCOME = "Hallo. Bei welchem Vorhaben kann ich dir helfen.";
    public static final String GENERAL_HELP = "Ich unterstütze dich im Alltag, du kannst mir sagen bei welchem Vorhaben ich dich begleiten soll.";
    public static final String HELP_REPROMPT = "Bitte sage mir welche Hilfe du brauchst.";
    public static final String END_SKILL = "Auf Wiedersehen";
    public static final String FALLBACK = "Tut mir leid, das weiss ich nicht. Sage einfach Hilfe.";
    public static final String NO_PROCEDURE_SELECTED = "Starte zuerst einen Vorgang. Wenn du nicht weiter weißt, sag einfach  Hilfe.";
    public static final String LAUNCH_ERROR = "Beim Laden der Daten ist ein Fehler aufgetreten.";
    public static final String NO_LOCAL_PROCEDURES_RESPONSE = "Keine Vorgänge gefunden, bitte erstelle neue Vorgänge";
}
