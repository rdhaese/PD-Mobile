package be.rdhaese.project.mobile.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import be.rdhaese.project.mobile.task.MarkAsLostTask;

/**
 * Created by RDEAX37 on 8/04/2016.
 */
public class DialogTool {

    public AlertDialog yesNoDialog(Activity activityContext, String title, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        //Create dialog
        AlertDialog dialog = new AlertDialog.Builder(activityContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", yesListener)
                .setNegativeButton("No", noListener)
                .create();

        //Edit dialog window:
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //Setting dialog location...
        switch (activityContext.getResources().getConfiguration().orientation){
            case 2: //ORIENTATION_LANDSCAPE
                //... to the right when in landscape mode
                wlp.gravity = Gravity.RIGHT;
                break;
            default: //In case of ORIENTATION_PORTRAIT or ORIENTATION_UNDEFINED
                //... to the bottom in any portrait or undefined mode
                wlp.gravity = Gravity.BOTTOM;
                break;
        }

        return dialog;
    }

    public AlertDialog yesDialog(Activity activityContext, String title, String message, DialogInterface.OnClickListener yesListener) {
        //Create dialog
        AlertDialog dialog = new AlertDialog.Builder(activityContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", yesListener)
                .create();

        //Edit dialog window:
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //Setting dialog location...
        switch (activityContext.getResources().getConfiguration().orientation){
            case 2: //ORIENTATION_LANDSCAPE
                //... to the right when in landscape mode
                wlp.gravity = Gravity.RIGHT;
                break;
            default: //In case of ORIENTATION_PORTRAIT or ORIENTATION_UNDEFINED
                //... to the bottom in any portrait or undefined mode
                wlp.gravity = Gravity.BOTTOM;
                break;
        }

        return dialog;
    }
}
