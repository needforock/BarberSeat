package ve.needforock.barberseat.views.user_detail;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.CurrentUser;
import ve.needforock.barberseat.data.Queries;
import ve.needforock.barberseat.data.UserToFireBase;
import ve.needforock.barberseat.models.Customer;
import ve.needforock.barberseat.views.appointment.JobFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailFragment extends Fragment {

    TextView name, mail, phone, number;
    LinearLayout phoneLL;
    FloatingActionButton save, edit;
    String userPhone;


    public UserDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CircularImageView circularImageView = view.findViewById(R.id.userAvatarCiv);
        name = view.findViewById(R.id.userNameTv);
        mail = view.findViewById(R.id.userMailTv);
        phone = view.findViewById(R.id.userPhoneEt);
        number = view.findViewById(R.id.numberTv);
        phoneLL = view.findViewById(R.id.phonLL);
        save = view.findViewById(R.id.saveFab);
        edit = view.findViewById(R.id.editFab);


        FirebaseUser firebaseUser = new CurrentUser().getCurrentUser();

        final Uri userImageUri = firebaseUser.getPhotoUrl();
        if(userImageUri!=null){

            Picasso.with(getContext()).load(userImageUri).into(circularImageView);
        }



        mail.setText(firebaseUser.getEmail());
        name.setText(firebaseUser.getDisplayName());
        String uid = firebaseUser.getUid();
        DatabaseReference ref = new Queries().UserDetails(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Customer customer = dataSnapshot.getValue(Customer.class);
                userPhone = customer.getPhone();
                if(userPhone!=null && userPhone.trim().length()>0){
                    number.setVisibility(View.VISIBLE);
                    number.setText(userPhone);
                    phone.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);

                }else{
                    phoneLL.setVisibility(View.GONE);
                    phone.setVisibility(View.VISIBLE);
                    phone.setText("");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userPhone = String.valueOf(phone.getText());
                new UserToFireBase().phoneToFireBase(userImageUri, userPhone);
                Toast.makeText(getContext(), "Guardado", Toast.LENGTH_SHORT).show();
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = JobFragment.class;
                setFragment(fragment, fragmentClass);


            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneLL.setVisibility(View.GONE);
                phone.setVisibility(View.VISIBLE);
                phone.setText(userPhone);
                edit.setVisibility(View.GONE);
            }
        });





    }


    public void setFragment (Fragment fragment, Class fragmentClass){

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }





}
