package ve.needforock.barberseat.views.user_detail;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Map;

import ve.needforock.barberseat.R;
import ve.needforock.barberseat.data.CurrentUser;
import ve.needforock.barberseat.data.SaveUserPhoto;
import ve.needforock.barberseat.data.UserToFireBase;
import ve.needforock.barberseat.views.job_selection.JobFragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailFragment extends Fragment implements SaveUserPhotoCallBack, UserCallBack {

    private TextView name, mail, phone, number;
    private LinearLayout phoneLL;
    private FloatingActionButton save, edit;
    private String userPhoneValid, path, userPhotoValid,userImageUrl;
    private CircularImageView circularImageView;
    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 20;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private UserPresenter userPresenter;
    private String uid;



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

        circularImageView = view.findViewById(R.id.userAvatarCiv);
        name = view.findViewById(R.id.userNameTv);
        mail = view.findViewById(R.id.userMailTv);
        phone = view.findViewById(R.id.userPhoneEt);
        number = view.findViewById(R.id.numberTv);
        phoneLL = view.findViewById(R.id.phonLL);
        save = view.findViewById(R.id.saveFab);
        edit = view.findViewById(R.id.editFab);


        firebaseUser = new CurrentUser().getCurrentUser();

        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE

        };

        magicalPermissions = new MagicalPermissions(this, permissions);
        magicalCamera = new MagicalCamera(getActivity(), RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPhoto();


            }
        });

        final Uri userImageUri = firebaseUser.getPhotoUrl();
        if(userImageUri!=null){

            Picasso.with(getContext()).load(userImageUri).into(circularImageView);
        }



        mail.setText(firebaseUser.getEmail());
        name.setText(firebaseUser.getDisplayName());
        uid = firebaseUser.getUid();
        userPresenter = new UserPresenter(this);
       userPresenter.startListening(uid);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userPhone = String.valueOf(phone.getText());
                if(userImageUrl !=null){
                    new UserToFireBase().phoneToFireBase(userImageUrl, userPhone);
                }else{
                    new UserToFireBase().phoneToFireBase("null", userPhone);
                }

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
                phone.setText(userPhoneValid);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        for (String permission : map.keySet()) {
            Log.d("PERMISSIONS", permission + " was: " + map.get(permission));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        magicalCamera.resultPhoto(requestCode, resultCode, data);
        magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_90);

        if (RESULT_OK == resultCode) {
            Bitmap photo = magicalCamera.getPhoto();
            path = magicalCamera.savePhotoInMemoryDevice(photo, "contactPhoto", "myDirectoryName", MagicalCamera.JPEG, true);
            path = "file://" + path;
            setPhoto(path);

        } else {
            Toast.makeText(getContext(), "Foto No Tomada", Toast.LENGTH_SHORT).show();
        }

    }
    private void requestPhoto() {
        magicalCamera.takeFragmentPhoto(UserDetailFragment.this);
    }

    private void setPhoto(String path){


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.show();

        new SaveUserPhoto(this).photoToFirebase(path, firebaseUser.getDisplayName().trim());
    }


    @Override
    public void saved(String photoUrl) {
        Log.d("URL", photoUrl);
        userImageUrl = photoUrl;

        Picasso.with(getContext()).load(photoUrl).centerCrop().fit().into(circularImageView);
        if(photoUrl.trim().length()>0){
            progressDialog.dismiss();
        }
        edit.setVisibility(View.GONE);
        phoneLL.setVisibility(View.GONE);
        phone.setVisibility(View.VISIBLE);
        phone.setText(userPhoneValid);

    }

    @Override
    public void photoNoNull(String photo) {
        userPhotoValid = photo;
        Picasso.with(getContext()).invalidate(photo);
        Picasso.with(getContext()).load(photo).into(circularImageView);

    }

    @Override
    public void userPhoneNoNull(String userPhone) {
        userPhoneValid = userPhone;
        number.setVisibility(View.VISIBLE);
        number.setText(userPhone);
        phone.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
    }

    @Override
    public void userPhoneNull() {
        phoneLL.setVisibility(View.GONE);
        phone.setVisibility(View.VISIBLE);
        phone.setText("");
        edit.setVisibility(View.GONE);
    }

    @Override
    public void userNull() {
    }

    @Override
    public void photoNull() {
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("PRUEBA", "onStop: fragment");
        userPresenter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("PRUEBA", "onStart: fragment");
        userPresenter.startListening(uid);
    }
}
