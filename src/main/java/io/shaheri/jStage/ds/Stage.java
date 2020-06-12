package io.shaheri.jStage.ds;

import io.shaheri.jStage.model.Input;
import io.shaheri.jStage.model.Output;
import io.shaheri.jStage.function.StageFunction;

import java.util.ArrayList;
import java.util.List;

public class Stage<T> {

    private String echo;
    private String name;
    private boolean isDone = false;
    private List<Input> inputs;
    private Output<T> output;
    private StageFunction<T> function;
    private List<Stage> predecessors;
    private List<Stage> successors;

    private Stage(){
        predecessors = new ArrayList<>();
        successors = new ArrayList<>();
    }

    public Stage(String name, String echo) {
        this();
        this.name = name;
        this.echo = echo;
    }

    public String getEcho() {
        return echo;
    }

    boolean isDone(){
        return this.isDone;
    }

     public void addSuccessor(Stage stage){
        successors.add(stage);
        stage.addPredecessor(this);
     }

     public List<Stage> getSuccessors(){
        return this.successors;
     }

     private void addPredecessor(Stage stage){
        predecessors.add(stage);
     }

    /**
     * @author Alireza Shaheri
     * If a stage is already executed, it cannot be executed again.
     * And also if all the predecessors of a stage are not executed properly (without unhandled exceptions), the stage also cannot be executed.
     * @return boolean which indicated whether a stage can be executed or not.
     */
    boolean canBeExecuted(){
        return !isDone && (predecessors == null || predecessors.isEmpty() || predecessors.stream().allMatch(stage -> stage.isDone));
    }

    public String getName() {
        return this.name;
    }
}