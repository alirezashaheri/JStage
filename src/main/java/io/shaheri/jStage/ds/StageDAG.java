package io.shaheri.jStage.ds;

import io.shaheri.jStage.exception.StageRuntimeException;
import io.shaheri.jStage.log.DAGLogger;
import io.shaheri.jStage.model.Output;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class StageDAG {

    private final String echo;
    private final String identifier;
    private final Map<String, Stage> stages;
    private DAGLogger logger;

    public StageDAG(String echo, String identifier) {
        this.echo = echo;
        this.identifier = identifier;
        stages = new HashMap<>();
    }

    public Map<String, Stage> getStages(List<String> names) {
        return stages
                .entrySet()
                .stream()
                .filter(stringStageEntry -> names.stream().anyMatch(k -> k.equals(stringStageEntry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void addStage(Stage stage) {
        stages.put(stage.getName(), stage);
    }

    public String getEcho() {
        return this.echo;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void build() {
        stages.forEach((key1, value1) -> value1.getPredecessors().forEach((key, value) -> value.addSuccessor(key1, value1)));
    }

    public Map<String, Output> execute() {
        if (logger != null)
            logger.onStart(this);
        ConcurrentHashMap<String, Output> outputs = new ConcurrentHashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(stages.size());
        List<CompletableFuture> futures = new ArrayList<>();
//        List<String> underProcess = Collections.synchronizedList(new ArrayList<>());
        ConcurrentHashMap<String, Stage> underProcess = new ConcurrentHashMap<>();
        while (!isDone()) {
            Map<String, Stage> toBeProcessed = readyToBeProcessed(stages.entrySet().stream()
                    .filter(entry -> underProcess.keySet().stream().noneMatch(s -> s.equals(entry.getKey())))
                    .collect(Collectors.toList()));
            underProcess.putAll(toBeProcessed);
            toBeProcessed.keySet().forEach(s -> futures.add(CompletableFuture.runAsync(() -> {
                try {
                    stages.get(s).exec();
                } catch (StageRuntimeException e) {
                    e.printStackTrace();
                }
            }, executorService)
                    .thenApply(a -> outputs.put(s, stages.get(s).getOutput().orElse(new Output(null))))
                    .thenApply(a -> underProcess.remove(s))));
        }
        futures.forEach(CompletableFuture::join);
        executorService.shutdown();
        if (logger != null)
            logger.onComplete(this);
        return outputs;
    }

    private Map<String, Stage> readyToBeProcessed(List<Map.Entry<String, Stage>> collected) {
        return collected.stream()
                .filter(entry -> (entry.getValue().getPredecessors() == null || entry.getValue().getPredecessors().isEmpty() || entry.getValue().getPredecessors().values().stream().allMatch(Stage::isDone)) && !entry.getValue().isDone())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private StageDAG logger(DAGLogger logger){
        this.logger = logger;
        return this;
    }

    private boolean isDone() {
        return stages.values().stream().allMatch(Stage::isDone);
    }

}
