package io.shaheri.jStage.function;

import io.shaheri.jStage.model.Input;
import io.shaheri.jStage.model.Output;

import java.util.Map;

@FunctionalInterface
public interface StageFunction {
    Output apply(Map<String, Output> predecessors);
}
