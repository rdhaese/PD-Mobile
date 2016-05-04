package be.rdhaese.project.mobile.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import java.util.Collection;

import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.constants.Constants;

/**
 * Created by RDEAX37 on 8/04/2016.
 */
public class DialogTool {

    public AlertDialog yesNoDialog(Context context, String title, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        //Create dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, yesListener)
                .setNegativeButton(R.string.no, noListener)
                .create();

        //Edit dialog location
        setDialogLocation(context, dialog);

        return dialog;
    }

    public AlertDialog yesDialog(Context context, String title, String message, DialogInterface.OnClickListener yesListener) {
        return oneButtonDialog(context, title, message, R.string.yes, yesListener);
    }

    public AlertDialog oneButtonDialog(Context context, String title, String message, String buttonText, DialogInterface.OnClickListener listener) {
        //Create dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, listener)
                .create();

        //Edit dialog location
        setDialogLocation(context, dialog);

        return dialog;
    }

    public AlertDialog oneButtonDialog(Context context, String title, String message, int buttonTextId, DialogInterface.OnClickListener listener) {
        //Create dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonTextId, listener)
                .create();

        //Edit dialog location
        setDialogLocation(context, dialog);

        return dialog;
    }

    public void setDialogLocation(Context context, AlertDialog dialog) {
        //Get LayoutParams of dialog's window
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //Setting dialog location...
        switch (context.getResources().getConfiguration().orientation){
            case Constants.ORIENTATION_LANDSCAPE: //ORIENTATION_LANDSCAPE
                //... to the right when in landscape mode
                wlp.gravity = Gravity.RIGHT;
                break;
            default: //In case of ORIENTATION_PORTRAIT or ORIENTATION_UNDEFINED
                //... to the bottom in any portrait or undefined mode
                wlp.gravity = Gravity.BOTTOM;
                break;
        }
    }

    public AlertDialog fatalBackEndExceptionDialog(final Activity context) {
        return oneButtonDialog(
                context,
                context.getString(R.string.back_end_problem),
                context.getString(R.string.could_not_communicate_with_back_end),
                R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.finishAffinity();
                        System.exit(0);
                    }
                }
        );
    }
}
