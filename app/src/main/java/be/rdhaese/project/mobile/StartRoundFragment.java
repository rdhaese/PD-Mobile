package be.rdhaese.project.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.rdhaese.packetdelivery.dto.AddressDTO;
import be.rdhaese.project.mobile.activity.NumberOfPacketsActivity;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.navigation.NavigationTool;
import be.rdhaese.project.mobile.task.GetCompanyAddressTask;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;
import be.rdhaese.project.mobile.toast.ToastTool;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class StartRoundFragment extends RoboFragment {

    @InjectView(R.id.btnStartNewRound)
    private Button btnStartNewRound;

    @InjectExtra(value = Constants.MESSAGE_KEY, optional = true)
    private String message;

    @InjectExtra(value = Constants.ROUND_FINISHED_KEY, optional = true)
    private Boolean roundFinished = false;

    private Boolean messageShown = false;

    private DialogTool dialogTool;
    private ToastTool toastTool;
    private NavigationTool navigationTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        toastTool = context.getBean(Constants.TOAST_TOOL_KEY);
        dialogTool = context.getBean(Constants.DIALOG_TOOL_KEY);
        navigationTool = context.getBean(Constants.NAVIGATION_TOOL_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_round, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            init();
        } catch (Exception e){
            dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
        }
    }

    private void init() throws Exception {
        if ((message != null) && (!messageShown)) {
            toastTool.createToast(getActivity(), message).show();
            messageShown =true;
        }

        btnStartNewRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRound(v);
            }
        });

        if (roundFinished) {
            //Set roundFinished to false so navigation only opens 1 time
            roundFinished = false;

            //Get company address
            AsyncTaskResult<AddressDTO> companyAddressResult = new GetCompanyAddressTask().execute().get();
            if (companyAddressResult.hasException()){
                throw companyAddressResult.getException();
            }
            AddressDTO companyAddress = companyAddressResult.getResult();

            //Navigate to address
            navigationTool.startNavigation(
                    getActivity(),
                    companyAddress.getStreet(),
                    companyAddress.getNumber(),
                    companyAddress.getPostalCode(),
                    companyAddress.getCity()
            );
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.ROUND_FINISHED_KEY, roundFinished);
        outState.putBoolean(Constants.MESSAGE_SHOWN_KEY, messageShown);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            roundFinished = savedInstanceState.getBoolean(Constants.ROUND_FINISHED_KEY);
            messageShown = savedInstanceState.getBoolean(Constants.MESSAGE_SHOWN_KEY);
        }
    }

    public void newRound(View view) {
        Intent intent = new Intent(getActivity(), NumberOfPacketsActivity.class);
        startActivity(intent);
    }
}
