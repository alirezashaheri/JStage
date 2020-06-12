package io.shaheri.jStage.model;

import java.lang.reflect.Type;

public class Argument<T> {

    private Type type;
    private T value;

    public Argument(T value) {
        this.type = value.getClass();
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public T getValue() {
        return value;
    }
}