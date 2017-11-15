package ve.needforock.barberseat.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.adapters.BarberAdapter;
import ve.needforock.barberseat.adapters.BarberListener;
import ve.needforock.barberseat.models.Barber;

public class BarberSelectionActivity extends AppCompatActivity implements BarberListener, SelectedDateCallBack {

    public static final String SELECTED_JOB = "ve.needforock.barberseat.views.KEY.SELECTED_JOB";

    private ArrayList<Barber> barbers;
    private RecyclerView recyclerView;
    private BarberAdapter barberAdapter;
    private CaldroidFragment dialogCaldroidFragment,caldroidFragment;
    private Bundle state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_selection);

        barbers = (ArrayList<Barber>) getIntent().getSerializableExtra(SELECTED_JOB);


        recyclerView = findViewById(R.id.barberRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        barberAdapter = new BarberAdapter(barbers, this);
        recyclerView.setAdapter(barberAdapter);
        state = savedInstanceState;
        caldroidFragment = new CaldroidFragment();



    }

    @Override
    public void barberClicked(String barberUid) {

        dialogCaldroidFragment = new CaldroidFragment();

        final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
        if (state != null) {
            dialogCaldroidFragment.restoreDialogStatesFromKey(
                    getSupportFragmentManager(), state,
                    "DIALOG_CALDROID_SAVED_STATE", dialogTag);
            Bundle args = dialogCaldroidFragment.getArguments();
            if (args == null) {
                args = new Bundle();
                dialogCaldroidFragment.setArguments(args);
            }
        } else {
            // Setup arguments
            Bundle bundle = new Bundle();
            // Setup dialogTitle
            dialogCaldroidFragment.setArguments(bundle);
        }

        dialogCaldroidFragment.show(getSupportFragmentManager(),
                dialogTag);

        new SetCalendar(this).Set(barberUid, dialogCaldroidFragment, BarberSelectionActivity.this);

    }


    @Override
    public void selectedDate(Date date) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        Toast.makeText(BarberSelectionActivity.this, "Fecha seleccionada " + formatter.format(date),
                Toast.LENGTH_SHORT).show();

    }
}
