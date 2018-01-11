package ve.needforock.barberseats.models;

import java.util.Date;

/**
 * Created by Soporte on 13-Nov-17.
 */

public class BarberDay {
    private boolean nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen, seventeen, eightteen, ninteen, twenty;
    private boolean fullDay;
    private Date date;

    public BarberDay() {
    }

    public boolean isNine() {
        return nine;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNine(boolean nine) {
        this.nine = nine;
    }

    public boolean isTen() {
        return ten;
    }

    public void setTen(boolean ten) {
        this.ten = ten;
    }

    public boolean isEleven() {
        return eleven;
    }

    public void setEleven(boolean eleven) {
        this.eleven = eleven;
    }

    public boolean isTwelve() {
        return twelve;
    }

    public void setTwelve(boolean twelve) {
        this.twelve = twelve;
    }

    public boolean isThirteen() {
        return thirteen;
    }

    public void setThirteen(boolean thirteen) {
        this.thirteen = thirteen;
    }

    public boolean isFourteen() {
        return fourteen;
    }

    public void setFourteen(boolean fourteen) {
        this.fourteen = fourteen;
    }

    public boolean isFifteen() {
        return fifteen;
    }

    public void setFifteen(boolean fifteen) {
        this.fifteen = fifteen;
    }

    public boolean isSixteen() {
        return sixteen;
    }

    public void setSixteen(boolean sixteen) {
        this.sixteen = sixteen;
    }

    public boolean isSeventeen() {
        return seventeen;
    }

    public void setSeventeen(boolean seventeen) {
        this.seventeen = seventeen;
    }

    public boolean isEightteen() {
        return eightteen;
    }

    public void setEightteen(boolean eightteen) {
        this.eightteen = eightteen;
    }

    public boolean isNinteen() {
        return ninteen;
    }

    public void setNinteen(boolean ninteen) {
        this.ninteen = ninteen;
    }

    public boolean isTwenty() {
        return twenty;
    }

    public void setTwenty(boolean twenty) {
        this.twenty = twenty;
    }

    public boolean isFullDay() {
        return fullDay;
    }

    public void setFullDay(boolean fullDay) {
        this.fullDay = fullDay;
    }
}
