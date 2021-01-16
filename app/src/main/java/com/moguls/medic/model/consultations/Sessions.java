package com.moguls.medic.model.consultations;

public class Sessions {

    String Day;
    StartEndTime Morning;
    StartEndTime AfterNoon;

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public StartEndTime getMorning() {
        return Morning;
    }

    public void setMorning(StartEndTime morning) {
        Morning = morning;
    }

    public StartEndTime getAfterNoon() {
        return AfterNoon;
    }

    public void setAfterNoon(StartEndTime afterNoon) {
        AfterNoon = afterNoon;
    }

    public StartEndTime getEvening() {
        return Evening;
    }

    public void setEvening(StartEndTime evening) {
        Evening = evening;
    }

    StartEndTime Evening;



}
