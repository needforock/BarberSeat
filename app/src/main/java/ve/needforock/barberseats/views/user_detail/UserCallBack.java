package ve.needforock.barberseats.views.user_detail;

/**
 * Created by Soporte on 16-Dec-17.
 */

public interface UserCallBack {

    void photoNoNull(String photo);
    void userPhoneNoNull(String phone);
    void userPhoneNull();
    void userNull();
    void photoNull();

}
