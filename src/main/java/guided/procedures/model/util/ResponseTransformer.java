package guided.procedures.model.util;

import guided.procedures.model.enumeration.SeverityLevel;

public class ResponseTransformer {

    private ResponseTransformer() {
    }

    private static ResponseTransformer responseTransformerInstance;
    private String initialResponseText = null;
    private String modifiedResponseText = null;
    private boolean speechRateModified = false;
    private boolean breakTimeModified = false;

    public static ResponseTransformer builder() {
        if (responseTransformerInstance == null) {
            responseTransformerInstance = new ResponseTransformer();
        }
        return responseTransformerInstance;
    }

    public ResponseTransformer setResponseText(String text) {
        initialResponseText = text;
        modifiedResponseText = text;
        return this;
    }

    public ResponseTransformer applySpeechRate(SeverityLevel level) {
        if (!speechRateModified) {
            speechRateModified = true;
            String prefix = "<prosody rate=\"";
            switch (level) {
                case MILD:
                    prefix += "medium\">";
                    break;
                case MODERATE:
                    prefix += "slow\">";
                    break;
                case SEVERE:
                    prefix += "x-slow\">";
                    break;
            }
            modifiedResponseText = prefix + initialResponseText + "</prosody>";
        }
        return responseTransformerInstance;
    }

    public ResponseTransformer applySpeechAfterBreakTime(String speech, Long breakTime) {
        //add a waiting period of breakTime in seconds, and a speech text to the response text
        if (!breakTimeModified) {
            breakTimeModified = true;
            if (speechRateModified) {
                modifiedResponseText = insertSpeechAfterBreakTimeIntoSpeechRateModifiedText(speech, breakTime);
            } else {
                modifiedResponseText = modifiedResponseText + (breakTime != null ?
                        parseConfirmationTimeToBreakTimeTag(breakTime) : "") + speech;
            }
        }
        return this;
    }

    private String insertSpeechAfterBreakTimeIntoSpeechRateModifiedText(String speech, Long breakTime) {
        int lastIdx = modifiedResponseText.lastIndexOf('/') - 1;
        String firstPart = modifiedResponseText.substring(0, lastIdx);
        String insert = (breakTime != null ?
                parseConfirmationTimeToBreakTimeTag(breakTime) : "") + speech;
        String lastPart = modifiedResponseText.substring(lastIdx);
        return firstPart + insert + lastPart;
    }

    private String parseConfirmationTimeToBreakTimeTag(long confirmationTime) {
        String tagBegin = "<break time=\"";
        String tagEnd = "s\"/>";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < confirmationTime / 10; i++) {
            stringBuilder.append(tagBegin).append("10").append(tagEnd);
        }
        stringBuilder.append((confirmationTime % 10 > 0) ? tagBegin + confirmationTime % 10 + tagEnd : "");
        return stringBuilder.toString();
    }

    private static void resetResponseTransformer() {
        responseTransformerInstance = null;
    }

    public String buildResponse() {
        resetResponseTransformer();
        return modifiedResponseText;
    }
}
