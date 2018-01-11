package ve.needforock.barberseats.adapters;

import ve.needforock.barberseats.models.Appointment;

/**
 * Created by Soporte on 21-Nov-17.
 */

public interface AppointmentListener {


    void deleteClicked(Appointment appointment);
    void viewClicked(Appointment appointment);
    void barberClicked(String barberUid);
}
