package be.rdhaese.project.mobile.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

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
                .setPositiveButton("Yes", yesListener)
                .setNegativeButton("No", noListener)
                .create();

        //Edit dialog location
        setDialogLocation(context, dialog);

        return dialog;
    }

    public AlertDialog yesDialog(Context context, String title, String message, DialogInterface.OnClickListener yesListener) {
        return oneButtonDialog(context, title, message, "Yes", yesListener);
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

    public void setDialogLocation(Context context, AlertDialog dialog) {
        //Get LayoutParams of dialog's window
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //Setting dialog location...
        switch (context.getResources().getConfiguration().orientation){
            case 2: //ORIENTATION_LANDSCAPE
                //... to the right when in landscape mode
                wlp.gravity = Gravity.RIGHT;
                break;
            default: //In case of ORIENTATION_PORTRAIT or ORIENTATION_UNDEFINED
                //... to the bottom in any portrait or undefined mode
                wlp.gravity = Gravity.BOTTOM;
                break;
        }
    }
}
