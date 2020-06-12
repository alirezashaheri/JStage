package io.shaheri.JStage.ds;

import io.shaheri.JStage.model.Input;
import io.shaheri.JStage.model.Output;
import io.shaheri.JStage.model.StageFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stage<T> {

    private String name;
    private boolean isDone = false;
    private List<Input> inputs;
    private Output<T> output;
    private StageFunction<T> function;
    private List<String> requirements;
    private Map<String, Output> dagOutputs;

    Stage(String name, List<String> requirements, List<Input> inputs, StageFunction<T> function) {
        this.name = name;
        this.requirements = requirements;
        this.inputs = inputs;
        this.function = function;
    }

    Stage(String name, List<Input> inputs, StageFunction<T> function) {
        this(name, new ArrayList<>(), inputs, function);
    }

    void injectDAGOutputMap(Map<String, Output> map){
        this.dagOutputs = map;
    }

    private void exec(){
        if (!isDone) {
            if (canBeExecuted()) {
                isDone = true;
                output = function.apply((Input[]) inputs.toArray());
            }
        }
    }

    private boolean canBeExecuted() {
        return requirements.stream().allMatch(key -> dagOutputs.containsKey(key));
    }

    private Map<String, Output> getRequirements(){
        return dagOutputs
                .entrySet()
                .stream()
                .filter(stringOutputEntry -> requirements.contains(stringOutputEntry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Output<T> get(){
        if (!isDone)
            exec();
        return output;
    }

    String getName(){
        return this.name;
    }

}