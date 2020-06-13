package io.shaheri.jStage.model;

import java.lang.reflect.Type;

public class Argument {

    private Object value;

    public Argument(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}