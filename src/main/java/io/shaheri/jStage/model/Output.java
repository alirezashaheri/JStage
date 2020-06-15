package io.shaheri.jStage.model;

public class Output extends Argument {

    private Exception exception;

    public Output(Object value) {
        super(value);
    }

    public Output(Exception e){
        super(null);
        exception = e;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
