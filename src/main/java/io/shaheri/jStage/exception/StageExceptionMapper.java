package io.shaheri.jStage.exception;

public interface StageExceptionMapper {

    UniversalException onException(Throwable throwable);

}
