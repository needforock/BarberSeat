package ve.needforock.barberseat.views.dialog_fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;

import ve.needforock.barberseat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReserveDialogFragment extends DialogFragment {

    private RadioGroup radioGroup;
    private TextView cancelTv, continueTv;
    private RadioButton cut, shave;
    private static Collection<String> jobs;
    private static ReserveCallBack reserveCallBack;

    public static ReserveDialogFragment newInstance(Collection<String> jobs, ReserveCallBack reserveCallBack) {
        ReserveDialogFragment.jobs = jobs;
        ReserveDialogFragment.reserveCallBack = reserveCallBack;
        return new ReserveDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserve_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroup = view.findViewById(R.id.jobRg);
        cancelTv = view.findViewById(R.id.cancelTv);
        continueTv = view.findViewById(R.id.continueTv);
        cut = radioGroup.findViewById(R.id.cutRB);
        shave = radioGroup.findViewById(R.id.shaveRB);


        if(jobs.contains("Corte")){

            cut.setText("Corte");
        }
        if (jobs.contains("Rasurado")){

            shave.setText("Rasurado");
        }


        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveCallBack.cancelClicked();
            }
        });

        continueTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = radioGroup.getCheckedRadioButtonId();
                if(id != -1){
                    RadioButton radioButton = radioGroup.findViewById(id);
                    String job = radioButton.getText().toString();
                    reserveCallBack.continueClicked(job);
                }else{
                    Toast.makeText(getContext(), "Debe Seleccionar una Especialidad", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}
