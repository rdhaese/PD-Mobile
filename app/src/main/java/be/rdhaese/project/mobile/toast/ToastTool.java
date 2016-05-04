package be.rdhaese.project.mobile.toast;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import be.rdhaese.project.mobile.constants.Constants;

/**
 * Created by RDEAX37 on 8/04/2016.
 */
public class ToastTool {

    public Toast createToast(Activity activityContext, String toastText) {
        Toast toast = Toast.makeText(activityContext, toastText, Toast.LENGTH_SHORT);

        //Set toast location...
        toast.setGravity(Gravity.TOP, Constants.TOAST_X_OFFSET, Constants.TOAST_Y_OFFSET);

        return toast;
    }
}
