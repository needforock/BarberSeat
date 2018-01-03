package ve.needforock.barberseat.views.user_detail;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class UserDetailActivity extends AppCompatActivity implements SaveUserPhotoCallBack, UserCallBack {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        getSupportActionBar().setTitle("Mi Perfil");

        circularImageView = findViewById(R.id.userAvatarCiv);
        name = findViewById(R.id.userNameTv);
        mail = findViewById(R.id.userMailTv);
        phone = findViewById(R.id.userPhoneEt);
        number = findViewById(R.id.numberTv);
        phoneLL = findViewById(R.id.phonLL);
        save = findViewById(R.id.saveFab);
        edit = findViewById(R.id.editFab);


        firebaseUser = new CurrentUser().getCurrentUser();

        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE

        };

        magicalPermissions = new MagicalPermissions(this, permissions);
        magicalCamera = new MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPhoto();


            }
        });

        final Uri userImageUri = firebaseUser.getPhotoUrl();
        if(userImageUri!=null){

            Picasso.with(this).load(userImageUri).into(circularImageView);
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

                Toast.makeText(UserDetailActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
                finish();


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
            Toast.makeText(UserDetailActivity.this, "Foto No Tomada", Toast.LENGTH_SHORT).show();
        }

    }
    private void requestPhoto() {
        magicalCamera.takePhoto();
    }

    private void setPhoto(String path){


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();

        new SaveUserPhoto(this).photoToFirebase(path, firebaseUser.getDisplayName().trim());
    }


    @Override
    public void saved(String photoUrl) {
        Log.d("URL", photoUrl);
        userImageUrl = photoUrl;

        Picasso.with(this).load(photoUrl).centerCrop().fit().into(circularImageView);
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
        Picasso.with(this).invalidate(photo);
        Picasso.with(this).load(photo).into(circularImageView);

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

        userPresenter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();

        userPresenter.startListening(uid);
    }
}