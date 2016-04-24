package be.rdhaese.project.mobile.activity;

import be.rdhaese.project.mobile.toast.ToastTool;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public abstract class AbstractNoGoingBackActivity extends AbstractActivity {

    @Override
    public void onBackPressed() {
        String toastText = "You can't go back during a delivery round.";
        toastTool.createToast(this, toastText).show();
    }
}
