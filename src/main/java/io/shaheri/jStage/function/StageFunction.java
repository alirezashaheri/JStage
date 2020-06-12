package io.shaheri.jStage.function;

import io.shaheri.jStage.model.Input;
import io.shaheri.jStage.model.Output;

import java.util.Map;

@FunctionalInterface
public interface StageFunction<T> {
    Output<T> apply(Map<String, Output> predecessors, Input...inputs);
}
