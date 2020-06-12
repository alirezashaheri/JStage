package io.shaheri.JStage.ds;

import io.shaheri.JStage.model.Output;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StageDAG {

    private boolean isFinished = false;
    private List<Stage> stages;
    private Map<String, Output> results;

    protected StageDAG(List<Stage> stages){
        this.stages = stages;
        results = new HashMap<>();
        this.stages.forEach(stage -> stage.injectDAGOutputMap(results));
    }

    public void execute(){
        stages.forEach(stage -> results.put(stage.getName(), stage.get()));
    }

    private Map<String, Output> getOutputsAsMap(){
        return results;
    }

    public Output getOutput(String stage){
        return getOutputsAsMap().get(stage);
    }

}
