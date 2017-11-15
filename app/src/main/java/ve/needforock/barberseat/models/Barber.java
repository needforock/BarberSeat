package ve.needforock.barberseat.models;

import java.io.Serializable;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class Barber implements Serializable{
    String mail, uid, name, phone;

    public Barber() {
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
