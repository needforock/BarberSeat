package ve.needforock.barberseat.data;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ve.needforock.barberseat.views.user_detail.SaveUserPhotoCallBack;

/**
 * Created by Soporte on 16-Dec-17.
 */

public class SaveUserPhoto {

    private SaveUserPhotoCallBack saveUserPhotoCallBack;

    public SaveUserPhoto(SaveUserPhotoCallBack saveUserPhotoCallBack) {
        this.saveUserPhotoCallBack = saveUserPhotoCallBack;
    }

    public void photoToFirebase(String path, String name) {

        if (path != null) {
            final CurrentUser currentUser = new CurrentUser();
            String folder = currentUser.sanitizedEmail(currentUser.userEmail() + "/");
            String photoName = name + ".png";
            String baseUrl = "gs://barberseat-a2756.appspot.com/users/" + folder;
            String refUrl = baseUrl + "profile_picture/" + photoName;
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(refUrl);
            storageRef.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String[] fullUrl = taskSnapshot.getDownloadUrl().toString().split("&token");
                    String photoUrl = fullUrl[0];
                    saveUserPhotoCallBack.saved(photoUrl);
                }
            });
        }
    }
}
