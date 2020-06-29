package io.shaheri.jStage.ds;

import io.shaheri.jStage.exception.StageExceptionMapper;
import io.shaheri.jStage.exception.StageRuntimeException;
import io.shaheri.jStage.exception.UniversalException;
import io.shaheri.jStage.function.StageAspectFunction;
import io.shaheri.jStage.function.StageFunction;
import io.shaheri.jStage.log.StageLogger;
import io.shaheri.jStage.model.Output;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
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
    private StageLogger logger;
    private Consumer<Throwable> exceptionally;
    private StageExceptionMapper exceptionMapper;

    public Stage(String echo, String name) {
        this.echo = echo;
        this.name = name;
        predecessors = new HashMap<>();
        successors = new HashMap<>();
        isDone = false;
    }

    public void setExceptionally(Consumer<Throwable> exceptionally){
        this.exceptionally = exceptionally;
    }

    public void addLogger(StageLogger logger){
        this.logger = logger;
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

    public void addExceptionMapper(StageExceptionMapper exceptionMapper){
        this.exceptionMapper = exceptionMapper;
    }

    public void exec() throws StageRuntimeException, UniversalException {
        if (!isDone) {
            if (logger != null)
                logger.onStart(echo, this);
            if (before != null)
                before.job(this);
            try {
                output = process.apply(predecessors.values().stream().collect(Collectors.toMap(Stage::getName, stage -> stage.getOutput().orElse(new Output(null)))));
                if (logger != null)
                    logger.onComplete(echo, this);
            }catch (Exception e){
                if (logger != null)
                    logger.onError(echo, this, e);
                if (exceptionally != null) {
                    exceptionally.accept(e);
                }
                output = new Output(e);
                if (exceptionMapper != null)
                    throw exceptionMapper.onException(e);
            }finally {
                this.isDone = true;
                if (logger != null)
                    logger.onFinish(echo, this);
            }
            if (after != null)
                after.job(this);
        }
    }
}