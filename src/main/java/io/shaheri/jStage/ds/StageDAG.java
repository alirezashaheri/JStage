package io.shaheri.jStage.ds;

import io.shaheri.jStage.function.StageAspectFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StageDAG {

    private String echo;
    private Stage start;
    private Stage finish;
    private StageAspectFunction before;
    private StageAspectFunction after;

    public StageDAG(String echo, Stage start) {
        this.echo = echo;
        this.start = start;
        this.finish = start;
    }

    public boolean isFinished(){
        return finish.isDone();
    }

    public Stage getStart(){
        return this.start;
    }

    public Map<String, Stage> getAllStages(){
        Map<String, Stage> stages = new HashMap<>();
        addNextLayerStages(stages, start);
        return stages;
    }

    private void addNextLayerStages(Map<String, Stage> stages, Stage s){
        stages.putAll((Map<String, Stage>) s.getSuccessors()
                .stream()
                .filter(o -> !((Stage) o).getName().equals(finish.getName()))
                .collect(Collectors.toMap(o -> ((Stage) o).getName(), o -> (Stage) o)));
        if (s.getSuccessors() != null && !s.getSuccessors().isEmpty())
            s.getSuccessors().forEach(o -> addNextLayerStages(stages, (Stage) o));
    }

}
