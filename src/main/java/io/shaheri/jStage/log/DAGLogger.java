package io.shaheri.jStage.log;

import io.shaheri.jStage.ds.StageDAG;

public interface DAGLogger {

    void onStart(StageDAG dag);
    void onComplete(StageDAG dag);

}
