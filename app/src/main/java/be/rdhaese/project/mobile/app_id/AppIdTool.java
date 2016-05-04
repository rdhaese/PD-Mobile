package be.rdhaese.project.mobile.app_id;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.task.GetNewAppIdTask;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 23/04/2016.
 */
public class AppIdTool {


    public static final String HAS_NO_ID_YET = "has-no-id-yet";

    public String getAppId(Context context) throws Exception {
        //Read app id from file
        String appId = null;
        InputStream is = null;
        InputStreamReader ipsr = null;
        BufferedReader br = null;
        try {
            is = context.openFileInput(Constants.FILE_APP_ID);
            ipsr = new InputStreamReader(is);
            br = new BufferedReader(ipsr);
            appId = br.readLine();
            br.close();
        } catch (FileNotFoundException fnfe) {
            return HAS_NO_ID_YET;
        } catch (Exception e) {
            Log.w("MYTAG", e);
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            if (br != null) {

                br.close();
            }

            if (ipsr != null) {

                ipsr.close();

            }
        }


        if (appId == null) {
            //If no app id present, request it from back end
            AsyncTaskResult<String> appIdResult = new GetNewAppIdTask().execute().get();
            if (appIdResult.hasException()) {
                throw appIdResult.getException();
            }
            appId = appIdResult.getResult();

            //Write the id to the file
            saveAppId(context, appId);
        }

        return appId;
    }

    public void saveAppId(Context context, String appId) throws Exception {
        //Write the id to the file
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(Constants.FILE_APP_ID, Context.MODE_PRIVATE);
            fos.write(appId.getBytes());
        } catch (IOException e) {
            //TODO log
            throw e;
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}
