package io.shaheri.jStage.ds;

import io.shaheri.jStage.function.StageAspectFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StageDAG {

    private String echo;
    private Map<String, Stage> stages;

    public StageDAG(String echo) {
        this.echo = echo;
        stages = new HashMap<>();
    }

    public Map<String, Stage> getStages(List<String> names){
        return stages
                .entrySet()
                .stream()
                .filter(stringStageEntry -> names.stream().anyMatch(k -> k.equals(stringStageEntry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void addStage(Stage stage){
        stages.put(stage.getName(), stage);
    }

    public String getEcho(){
        return this.echo;
    }
}
