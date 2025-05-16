package org.tfg.timetrackapi.entity;


import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
public class EmployeeSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime hora;

    private DayOfWeek dia;

    @ManyToOne
    private User user;

    public EmployeeSchedule() {
    }

    public EmployeeSchedule(Long id, LocalTime hora, DayOfWeek dia, User user) {
        this.id = id;
        this.hora = hora;
        this.dia = dia;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DayOfWeek getDia() {
        return dia;
    }

    public void setDia(DayOfWeek dia) {
        this.dia = dia;
    }
}
