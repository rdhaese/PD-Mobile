package be.rdhaese.project.mobile.activity;

import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.toast.ToastTool;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public abstract class AbstractNoGoingBackActivity extends AbstractActivity {

    @Override
    public void onBackPressed() {
        String toastText = getString(R.string.cant_go_back_during_round);
        toastTool.createToast(this, toastText).show();
    }
}
