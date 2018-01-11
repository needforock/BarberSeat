package ve.needforock.barberseats.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class Job implements Serializable {
    private String name;
    private List<Barber> barberList;


    public Job() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Barber> getBarberList() {
        return barberList;
    }

    public void setBarberList(List<Barber> barberList) {
        this.barberList = barberList;
    }
}
