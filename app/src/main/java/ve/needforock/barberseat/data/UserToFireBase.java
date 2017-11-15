package ve.needforock.barberseat.data;

import android.net.Uri;

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
        new Nodes().user(key).child("details").setValue(customer);

    }
}
