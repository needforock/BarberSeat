package ve.needforock.barberseats.views.appointment_detail;

/**
 * Created by Soporte on 02-Dec-17.
 */

public class DayValidation {
    private DayCallBack dayCallBack;

    public DayValidation(DayCallBack dayCallBack) {
        this.dayCallBack = dayCallBack;
    }

    public void validate(int day){

        switch (day){
            case 1:
                dayCallBack.dayInString("Domingo");
                break;
            case 2:
                dayCallBack.dayInString("Lunes");
                break;
            case 3:
                dayCallBack.dayInString("Martes");
                break;
            case 4:
                dayCallBack.dayInString("Miercoles");
                break;
            case 5:
                dayCallBack.dayInString("Jueves");
                break;
            case 6:
                dayCallBack.dayInString("Viernes");
                break;
            case 7:
                dayCallBack.dayInString("Sabado");
                break;
        }

    }
}
