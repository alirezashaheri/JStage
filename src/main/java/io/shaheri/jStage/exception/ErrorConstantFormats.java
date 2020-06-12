package io.shaheri.jStage.exception;

public class ErrorConstantFormats {

    private static final String DUPLICATE_STAGE_NAME = "Duplicate stage name: %s .";
    private static final String UNCOMMITTED_STAGE = "Stage (%s) is not committed.";
    private static final String NOT_DEFINED_PREDECESSOR = "Stage (%s) has introduced some not defined predecessor stages.";

    public static String getDuplicateStageName(String name){
        return String.format(DUPLICATE_STAGE_NAME, name);
    }

    public static String getUncommittedStage(String name) {
        return String.format(UNCOMMITTED_STAGE, name);
    }

    public static String getNotDefinedPredecessor(String name) {
        return String.format(NOT_DEFINED_PREDECESSOR, name);
    }
}
