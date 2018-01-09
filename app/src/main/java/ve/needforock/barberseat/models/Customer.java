package ve.needforock.barberseat.models;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class Customer {
    private String Uid, name, phone, email, photo;
    private int appointments;

    public Customer() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
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

    public int getAppointments() {
        return appointments;
    }

    public void setAppointments(int appointments) {
        this.appointments = appointments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
