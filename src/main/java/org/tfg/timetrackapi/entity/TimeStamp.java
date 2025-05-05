package org.tfg.timetrackapi.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String isMod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User employee;

    public TimeStamp() {
    }

    public TimeStamp(Long id, LocalDateTime timestamp, String isMod, User employee) {
        this.id = id;
        this.timestamp = timestamp;
        this.isMod = isMod;
        this.employee = employee;
    }

    public String isMod() {
        return isMod;
    }

    public void setMod(String mod) {
        isMod = mod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}
