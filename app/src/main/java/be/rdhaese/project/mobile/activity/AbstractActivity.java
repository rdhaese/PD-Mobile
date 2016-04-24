package be.rdhaese.project.mobile.activity;

import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.toast.ToastTool;
import roboguice.activity.RoboFragmentActivity;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public abstract class AbstractActivity extends RoboFragmentActivity {

    protected DialogTool dialogTool;
    protected ToastTool toastTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        dialogTool = context.getBean("dialogTool");
        toastTool = context.getBean("toastTool");
    }
}
