package io.shaheri.jStage.builder;

import io.shaheri.jStage.ds.Stage;
import io.shaheri.jStage.ds.StageDAG;
import io.shaheri.jStage.exception.ErrorConstantFormats;
import io.shaheri.jStage.exception.StageBuilderException;

import java.util.UUID;

public class JStageSyncBuilder {

    private Stage temp;
    private StageDAG graph;

    public JStageSyncBuilder(String echo) {
        graph = new StageDAG(echo, new Stage(UUID.randomUUID().toString(), echo));
    }

    public JStageSyncBuilder stage(String name){
        checkStageConditions(name);
        temp = new Stage(graph.getStart().getEcho(), name);
        return this;
    }

    private void checkStageConditions(String name) {
        if (graph.getAllStages().containsKey(name))
            throw new StageBuilderException(ErrorConstantFormats.getDuplicateStageName(name));
        if (temp != null)
            throw new StageBuilderException(ErrorConstantFormats.getUncommittedStage(temp.getName()));
    }


}
