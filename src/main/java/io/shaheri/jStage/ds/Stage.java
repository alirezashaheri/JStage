package io.shaheri.jStage.ds;

import io.shaheri.jStage.function.StageAspectFunction;
import io.shaheri.jStage.function.StageFunction;
import io.shaheri.jStage.model.Output;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Stage {

    private final String echo;
    private final String name;
    private final Map<String, Stage> predecessors;
    private final Map<String, Stage> successors;
    private boolean isDone;
    private StageAspectFunction before;
    private StageAspectFunction after;
    private StageFunction process;
    private Output output;
    private Exception exception;

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

    public <T> Optional<T> getOutput(Class<T> type){
        return isDone ? Optional.of(type.cast(output.getValue())) : Optional.empty();
    }

    public Optional<Output> getOutput(){
        return isDone ? Optional.of(output) : Optional.empty();
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

    public Map<String, Stage> getPredecessors(){
        return this.predecessors;
    }

    public void addSuccessor(String name, Stage stage){
        this.successors.put(name, stage);
    }

    public boolean isDone(){
        return isDone;
    }

    public void exec() {
        if (!isDone) {
            if (before != null)
                before.job(this);
            try {
                output = process.apply(predecessors.values().stream().collect(Collectors.toMap(Stage::getName, stage -> stage.getOutput().orElse(new Output(null)))));
            }catch (Exception e){
                output = new Output(e);
            }finally {
                this.isDone = true;
            }
            if (after != null)
                after.job(this);
        }
    }
}