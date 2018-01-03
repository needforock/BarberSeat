package ve.needforock.barberseat.views.user_detail;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ve.needforock.barberseat.data.Queries;
import ve.needforock.barberseat.models.Customer;

/**
 * Created by Soporte on 16-Dec-17.
 */

public class UserPresenter implements ValueEventListener {

    private UserCallBack userCallBack;
    private DatabaseReference reference;

    public UserPresenter(UserCallBack userCallBack) {
        this.userCallBack = userCallBack;
    }

    public void startListening(String userUid){
        reference = new Queries().UserDetails(userUid);
        reference.addListenerForSingleValueEvent(this);
    }

    public void stopListening() {
        reference.removeEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        Customer customer = dataSnapshot.getValue(Customer.class);
        if(customer==null){
            userCallBack.userNull();
        }else {
            if(customer.getPhoto().equals("null")||customer.getPhoto()==null){
                userCallBack.photoNull();

            }else{

                userCallBack.photoNoNull(customer.getPhoto());
            }
            String userPhone = customer.getPhone();
            if(userPhone!=null && userPhone.trim().length()>0){
                userCallBack.userPhoneNoNull(userPhone);

            }else{
                userCallBack.userPhoneNull();


            }
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
