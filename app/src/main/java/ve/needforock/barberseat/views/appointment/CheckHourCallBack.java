package ve.needforock.barberseat.views.appointment;

import java.util.Date;

/**
 * Created by Soporte on 27-Nov-17.
 */

public interface CheckHourCallBack {

    void available(String hour, Date date);
    void noAvailable();
}
