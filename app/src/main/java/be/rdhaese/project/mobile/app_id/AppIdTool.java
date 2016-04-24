package be.rdhaese.project.mobile.app_id;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.task.GetNewAppIdTask;

/**
 * Created by RDEAX37 on 23/04/2016.
 */
public class AppIdTool {


    public String getAppId(Context context) {
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
        } catch (IOException e) {
            e.printStackTrace(); //TODO handle this
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace(); //TODO handle this
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();//TODO handle this
                }
            }
            if (ipsr != null) {
                try {
                    ipsr.close();
                } catch (IOException e) {
                    e.printStackTrace(); //TODO handle this
                }
            }
        }

        if (appId == null) {
            //If no app id present, request it from back end
            try {
                appId = new GetNewAppIdTask().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();//TODO handle this
            } catch (ExecutionException e) {
                e.printStackTrace(); //TODO handle this
            }

            //Write the id to the file
           saveAppId(context, appId);
        }

        return appId;
    }

    public void saveAppId(Context context, String appId) {
//Write the id to the file
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(Constants.FILE_APP_ID, Context.MODE_PRIVATE);
            fos.write(appId.getBytes());
        } catch (IOException e) {
            e.printStackTrace(); //TODO handle this
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();//TODO handle this
                }
            }
        }
    }
}
