package io.shaheri.jStage.builder;

import io.shaheri.jStage.ds.Stage;
import io.shaheri.jStage.ds.StageDAG;
import io.shaheri.jStage.exception.ErrorConstantFormats;
import io.shaheri.jStage.exception.StageBuilderException;
import io.shaheri.jStage.exception.StageRuntimeException;
import io.shaheri.jStage.function.StageAspectFunction;
import io.shaheri.jStage.function.StageFunction;
import io.shaheri.jStage.log.StageLogger;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DAGBuilder {

    private Stage temp;
    private final StageDAG graph;

    public DAGBuilder(String echo){
        graph = new StageDAG(echo);
    }

    public DAGBuilder stage(String name){
        if (temp != null)
            throw new StageBuilderException(ErrorConstantFormats.getUncommittedStage(name));
        temp = new Stage(graph.getEcho(), name);
        return this;
    }

    public DAGBuilder process(StageFunction process){
        temp.setProcess(process);
        return this;
    }

    public DAGBuilder before(StageAspectFunction function){
        temp.setBefore(function);
        return this;
    }

    public DAGBuilder after(StageAspectFunction function){
        temp.setAfter(function);
        return this;
    }

    public DAGBuilder predecessors(List<String> stages){
        temp.addPredecessors(graph.getStages(stages));
        return this;
    }

    public DAGBuilder exceptionally(Consumer<Throwable> exceptionally){
        temp.setExceptionally(exceptionally);
        return this;
    }

    public DAGBuilder exceptionMapper(Function<Throwable, StageRuntimeException> exceptionMapper){
        temp.addExceptionMapper(exceptionMapper);
        return this;
    }

    public DAGBuilder logger(StageLogger logger){
        temp.addLogger(logger);
        return this;
    }

    public DAGBuilder commitStage(){
        graph.addStage(temp);
        temp = null;
        return this;
    }

    public StageDAG build(){
        graph.build();
        return graph;
    }

}
