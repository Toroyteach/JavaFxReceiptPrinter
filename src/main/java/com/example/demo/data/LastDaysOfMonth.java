package com.example.demo.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LastDaysOfMonth {

    private LocalDate firstDay;
    private LocalDate lastDay;

    public LastDaysOfMonth() {

    }

    public LastDaysOfMonth(LocalDate firstDay, LocalDate lastDay) {
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

    public String getFirstDay() {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
        String output = dtf.format(firstDay);
        return firstDay.toString();
    }

    public void setFirstDay(LocalDate firstDay) {
        this.firstDay = firstDay;
    }

    public String getLastDay() {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
        String output = dtf.format(lastDay);
        return lastDay.toString();
    }

    public void setLastDay(LocalDate lastDay) {
        this.lastDay = lastDay;
    }
}
