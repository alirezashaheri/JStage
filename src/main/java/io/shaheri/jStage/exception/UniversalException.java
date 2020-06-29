package io.shaheri.jStage.exception;

import java.util.HashMap;

public class UniversalException extends Exception {

    private final String code;
    private final HashMap<String, String> messages;

    public UniversalException(String code) {
        this.messages = new HashMap<>();
        this.code = code;
    }

    public void addMessage(String languageCode, String message){
        messages.put(languageCode.toLowerCase(), message);
    }

    public String getCode() {
        return code;
    }

    public HashMap<String, String> getMessages() {
        return messages;
    }
}
