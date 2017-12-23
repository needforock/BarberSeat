package ve.needforock.barberseat.views.day;

import java.util.Map;

import ve.needforock.barberseat.models.Barber;

/**
 * Created by Soporte on 23-Dec-17.
 */

public interface BarberCallBack {

    void dayChecked(Map<String, Boolean> map);
    void barberChecked(Barber barber);
    void jobsChecked(String specialties);
}
