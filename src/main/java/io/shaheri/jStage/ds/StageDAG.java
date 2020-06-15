package io.shaheri.jStage.ds;

import io.shaheri.jStage.function.StageAspectFunction;
import io.shaheri.jStage.model.Output;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class StageDAG {

    private final String echo;
    private final Map<String, Stage> stages;

    public StageDAG(String echo) {
        this.echo = echo;
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

    public void build() {
        stages.forEach((key1, value1) -> value1.getPredecessors().forEach((key, value) -> value.addSuccessor(key1, value1)));
    }

    public Map<String, Output> execute() {
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
            toBeProcessed.keySet().forEach(s -> futures.add(CompletableFuture.runAsync(() -> stages.get(s).exec(), executorService)
                    .thenApply(a -> outputs.put(s, stages.get(s).getOutput().orElse(new Output(null))))
                    .thenApply(a -> underProcess.remove(s))));
        }
        futures.forEach(CompletableFuture::join);
        executorService.shutdown();
        return outputs;
    }

    private Map<String, Stage> readyToBeProcessed(List<Map.Entry<String, Stage>> collected) {
        return collected.stream()
                .filter(entry -> (entry.getValue().getPredecessors() == null || entry.getValue().getPredecessors().isEmpty() || entry.getValue().getPredecessors().values().stream().allMatch(Stage::isDone)) && !entry.getValue().isDone())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isDone() {
        return stages.values().stream().allMatch(Stage::isDone);
    }

}
