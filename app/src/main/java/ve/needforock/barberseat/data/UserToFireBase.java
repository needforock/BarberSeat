package ve.needforock.barberseat.data;

import android.net.Uri;

import com.google.firebase.database.ServerValue;

import ve.needforock.barberseat.models.Customer;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class UserToFireBase {
    public void SaveUserToFireBase(Uri userImageUri){
        CurrentUser currentUser = new CurrentUser();
        Customer customer = new Customer();
        customer.setEmail(currentUser.userEmail());
        customer.setName(currentUser.getCurrentUser().getDisplayName());
        customer.setPhoto(String.valueOf(userImageUri));
        customer.setUid(currentUser.getUid());

        String key = currentUser.getUid();
        new Nodes().user(key).setValue(customer);

    }

    public void phoneToFireBase(String userImageUrl , String phone){
        CurrentUser currentUser = new CurrentUser();

        Customer customer = new Customer();
        customer.setEmail(currentUser.userEmail());
        customer.setName(currentUser.getCurrentUser().getDisplayName());
        customer.setPhoto(userImageUrl);
        customer.setUid(currentUser.getUid());
        customer.setPhone(phone);
        String key = currentUser.getUid();
        new Nodes().user(key).setValue(customer);
        new Nodes().user(currentUser.getUid()).child("timeStamp").setValue(ServerValue.TIMESTAMP);

    }
}
