package ve.needforock.barberseat.adapters;

import ve.needforock.barberseat.models.Appointment;

/**
 * Created by Soporte on 21-Nov-17.
 */

public interface AppointmentListener {


    void deleteClicked(Appointment appointment);
    void viewClicked(Appointment appointment);
    void barberClicked(String barberUid);
}
