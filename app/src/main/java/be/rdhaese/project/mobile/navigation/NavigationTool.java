package be.rdhaese.project.mobile.navigation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.toast.ToastTool;

/**
 * Created by RDEAX37 on 1/05/2016.
 */
public class NavigationTool {

    private ToastTool toastTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        toastTool = context.getBean(Constants.TOAST_TOOL_KEY);
    }

    public void startNavigation(final Activity activity, String street, String number, String postalCode, String city){
        String qry = String.format(
                "%s%s+%s,+%s+%s",
                Constants.GOOGLE_NAVIGATION_QUERY_START,
                street,
                number,
                postalCode,
                city);
        Uri gmmIntentUri = Uri.parse(qry);

        //Create intent
        final Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage(Constants.PACKAGE_MAPS);

        //Show toast that navigation is going to start
        String toastText = activity.getString(R.string.navigation_is_starting);
        toastTool.createToast(activity, toastText).show();

        //Wait 3 seconds before starting navigation
        // so the courier has the time to understand what is happening
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Start navigation
                activity.startActivity(mapIntent);
            }
        }, Constants.NAVIGATION_START_DELAY_MILLIS);
    }
}
