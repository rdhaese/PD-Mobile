package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;
import android.util.Log;

import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 28/04/2016.
 */
public abstract class AbstractAsyncTask<S, T, U> extends AsyncTask<S, T, AsyncTaskResult<U>> {

    public AsyncTaskResult<U> createResult(U result) {
        String logText;
        if (result == null) {
            logText = "Result: [null]";
        } else {
            logText = String.format("Result: [%s]", result.toString());
        }
        Log.i(getClass().getSimpleName(), logText);

        return new AsyncTaskResult<U>(result);
    }

    public AsyncTaskResult<U> createResult(Exception e) {
        Log.w(getClass().getSimpleName(), "Exception:", e);

        return new AsyncTaskResult<U>(e);
    }

    public AsyncTaskResult<U> createNullResult() {
        Log.i(getClass().getSimpleName(), "Returning null as result");

        return new AsyncTaskResult<>();
    }

    public void logParams(Object... params) {
        Log.d(getClass().getSimpleName(), "Arguments:");
        for (int index = 0; index < params.length; index++) {
            String logText = String.format(
                    "Arg: [Index: [%s]; Type: [%s]; Value: [%s]]",
                    index,
                    params[index].getClass().getSimpleName(),
                    params[index].toString()
            );
            Log.d(getClass().getSimpleName(), logText);
        }
    }

    public Boolean isAmountOfParamsIncorrect(Integer correctAmount, Object... params) {
        logParams(params);
        if (correctAmount != params.length) {
            String logText = String.format(
                    "Amount of arguments not correct. Correct: [%s]; Actual: [%s]",
                    correctAmount,
                    params.length
            );
            Log.w(getClass().getSimpleName(), logText);
            return true;
        }

        String logText = String.format(
                "Amount of arguments [%s] correct",
                params.length
        );
        Log.d(getClass().getSimpleName(), logText);

        return false;
    }

    @Override
    protected void onPreExecute() {
        Log.i(getClass().getSimpleName(), "Executing...");
        super.onPreExecute();
    }
}
