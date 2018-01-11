package ve.needforock.barberseats.models;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class Barber implements Serializable{
    private String mail, uid, name, phone;
    private String photo;
    private Map<String, String> jobs;

    public Barber() {
    }

    public String getPhoto() {
        return photo;
    }

    public Map<String, String> getJobs() {
        return jobs;
    }

    public void setJobs(Map<String, String> jobs) {
        this.jobs = jobs;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
