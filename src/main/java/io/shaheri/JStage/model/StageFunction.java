package io.shaheri.JStage.model;

@FunctionalInterface
public interface StageFunction<T> {
    Output<T> apply(Input...inputs);
}
