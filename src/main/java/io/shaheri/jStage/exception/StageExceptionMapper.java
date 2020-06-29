package io.shaheri.jStage.exception;

import io.shaheri.jStage.ds.Stage;

public interface StageExceptionMapper {

    UniversalException onException(Stage stage, Throwable throwable);

}
