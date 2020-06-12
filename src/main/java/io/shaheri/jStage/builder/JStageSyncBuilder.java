package io.shaheri.jStage.builder;

import io.shaheri.jStage.ds.Stage;
import io.shaheri.jStage.ds.StageDAG;
import io.shaheri.jStage.exception.ErrorConstantFormats;
import io.shaheri.jStage.exception.StageBuilderException;
import io.shaheri.jStage.function.StageFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JStageSyncBuilder {

    private Stage temp;
    private StageDAG graph;
    private Map<String, Stage> stages;

    public JStageSyncBuilder(String echo) {
        graph = new StageDAG(echo, new Stage(UUID.randomUUID().toString(), echo));
        stages = new HashMap<>();
    }

    public JStageSyncBuilder stage(String name){
        checkStageConditions(name);
        temp = new Stage(graph.getStart().getEcho(), name);
        return this;
    }

    public JStageSyncBuilder duty(StageFunction duty){
        temp.setFunction(duty);
        return this;
    }

    public JStageSyncBuilder predecessors(List<String> predecessors){
        checkStageConditions(stages, predecessors);
        predecessors.forEach(predecessor -> temp.addPredecessor(stages.get(predecessor)));
        return this;
    }

    public JStageSyncBuilder commitStage(){
        stages.put(temp.getName(), temp);
        temp = null;
        return this;
    }

    public StageDAG build(){
        return convertMapToGraph(stages);
    }

    private StageDAG convertMapToGraph(Map<String, Stage> stages) {
        return null; //TODO
    }

    private Map<String, Stage> checkStageConditions(Map<String, Stage> stages, List<String> predecessors) {
        if (!predecessors.stream().allMatch(stages::containsKey))
            throw new StageBuilderException(ErrorConstantFormats.getNotDefinedPredecessor(temp.getName()));
        return stages;
    }

    private Map<String, Stage> checkStageConditions(String name) {
        if (stages.containsKey(name))
            throw new StageBuilderException(ErrorConstantFormats.getDuplicateStageName(name));
        if (temp != null)
            throw new StageBuilderException(ErrorConstantFormats.getUncommittedStage(temp.getName()));
        return stages;
    }


}
