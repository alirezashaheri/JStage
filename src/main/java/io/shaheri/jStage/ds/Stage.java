package io.shaheri.jStage.ds;

import io.shaheri.jStage.function.StageAspectFunction;
import io.shaheri.jStage.function.StageFunction;
import io.shaheri.jStage.model.Output;

import java.util.HashMap;
import java.util.Map;

public class Stage {

    private String echo;
    private String name;
    private Map<String, Stage> predecessors;
    private Map<String, Stage> successors;
    private boolean isDone;
    private StageAspectFunction before;
    private StageAspectFunction after;
    private StageFunction process;
    private Output output;

    public Stage(String echo, String name) {
        this.echo = echo;
        this.name = name;
        predecessors = new HashMap<>();
        successors = new HashMap<>();
        isDone = false;
    }

    public void setProcess(StageFunction function){
        this.process = function;
    }

    public void setBefore(StageAspectFunction before) {
        this.before = before;
    }

    public void setAfter(StageAspectFunction after) {
        this.after = after;
    }

    public <T> T getOutput(Class<T> type){
        return type.cast(output.getValue());
    }

    public void addPredecessor(Stage stage){
        predecessors.put(stage.name, stage);
    }

    public void addPredecessors(Map<String, Stage> predecessors){
        this.predecessors.putAll(predecessors);
    }

    public String getEcho() {
        return echo;
    }

    public String getName() {
        return name;
    }
}