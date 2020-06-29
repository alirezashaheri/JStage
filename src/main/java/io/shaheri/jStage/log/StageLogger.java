package io.shaheri.jStage.log;

import io.shaheri.jStage.ds.Stage;

public interface StageLogger {

    void onStart(String key, Stage input);
    void onComplete(String key, Stage input);
    void onError(String key, Stage stage, Throwable e);
    void onFinish(String key, Stage input);

}