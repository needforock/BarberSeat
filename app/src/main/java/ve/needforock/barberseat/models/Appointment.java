package ve.needforock.barberseat.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class Appointment implements Serializable {
    private String userUID, job, barberUid, barberName;
    private Date date;

    public Appointment() {
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getBarberUid() {
        return barberUid;
    }

    public void setBarberUid(String barberUid) {
        this.barberUid = barberUid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }
}
