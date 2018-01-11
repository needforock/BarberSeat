package ve.needforock.barberseats.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class Appointment implements Serializable {
    private String userUID, job, barberUid, barberName, key;
    private Date date;
    private boolean rated;

    public Appointment() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }
}
