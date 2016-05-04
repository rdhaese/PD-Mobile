package be.rdhaese.project.mobile.task.result;

/**
 * Created by RDEAX37 on 28/04/2016.
 */
public class AsyncTaskResult<T> {

    private T result;
    private Exception exception;

    /**
     * Use this constructor for null result
     */
    public AsyncTaskResult(){
    }

    public AsyncTaskResult(T result) {
        this.result = result;
    }

    public AsyncTaskResult(Exception exception) {
        this.exception = exception;
    }

    public T getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }

    public Boolean hasException(){
        return exception != null;
    }

    public Boolean hasNullResult(){
        return result == null;
    }
}
