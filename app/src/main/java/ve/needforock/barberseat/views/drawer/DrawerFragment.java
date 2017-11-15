package ve.needforock.barberseat.views.drawer;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.CurrentUser;
import ve.needforock.barberseat.data.UserToFireBase;
import ve.needforock.barberseat.views.login.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {


    public DrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView email = (TextView) view.findViewById(R.id.emailTv);
        TextView userName = (TextView) view.findViewById(R.id.userNameTv);
        FirebaseUser firebaseUser = new CurrentUser().getCurrentUser();
        CircularImageView circularImageView = (CircularImageView) view.findViewById(R.id.avatarCi);
        Uri userImageUri = firebaseUser.getPhotoUrl();
        if(userImageUri!=null){

            Picasso.with(getContext()).load(userImageUri).into(circularImageView);
        }


        email.setText(firebaseUser.getEmail());
        userName.setText(firebaseUser.getDisplayName());
        new UserToFireBase().SaveUserToFireBase(userImageUri);

        TextView signOut = (TextView) view.findViewById(R.id.logoutTv);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                getActivity().finish();
                            }

                        });

            }
        });

    }
}
