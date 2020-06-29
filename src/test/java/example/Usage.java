package example;

import io.shaheri.jStage.builder.DAGBuilder;
import io.shaheri.jStage.model.Output;

import java.util.Collections;
import java.util.Map;

public class Usage {

    public static void main(String[] args) {
        Map<String, Output> outputs = new DAGBuilder("testing usage", "test")
                .stage("1")
                .before(stage -> System.out.println(String.format("Starting stage %s ...", stage.getName())))
                .process(predecessors -> new Output("Success"))
                .after(stage -> System.out.println(String.format("Stage %s Finished.", stage.getName())))
                .commitStage()
                .stage("2")
                .predecessors(Collections.singletonList("1"))
                .before(stage -> System.out.println(String.format("Starting stage %s ...", stage.getName())))
                .process(predecessors -> new Output("Success"))
                .after(stage -> System.out.println(String.format("Stage %s Finished.", stage.getName())))
                .commitStage()
                .build()
                .execute();
        outputs.values().forEach(output -> System.out.println(output.getValue()));
    }

}
