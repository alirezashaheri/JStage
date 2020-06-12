package io.shaheri.jStage.function;

import io.shaheri.jStage.ds.Stage;

@FunctionalInterface
public interface StageAspectFunction {
    void job(Stage stage);
}
