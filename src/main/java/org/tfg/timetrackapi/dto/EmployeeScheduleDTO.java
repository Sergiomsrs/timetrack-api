package org.tfg.timetrackapi.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class EmployeeScheduleDTO {

    private String dni;
    private LocalTime hora;
    private DayOfWeek dia;

    private int dayNumber;

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public DayOfWeek getDia() {
        return dia;
    }

    public void setDia(DayOfWeek dia) {
        this.dia = dia;
    }
}
