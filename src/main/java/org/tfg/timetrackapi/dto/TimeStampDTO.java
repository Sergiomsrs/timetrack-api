package org.tfg.timetrackapi.dto;

import org.tfg.timetrackapi.entity.TimeStamp;

import java.time.LocalDateTime;

public class TimeStampDTO {

    private Long id;
    private LocalDateTime timestamp;
    private Long employeeId;

    private String isMod;


    public TimeStampDTO() {
    }

    public TimeStampDTO(Long id, LocalDateTime timestamp, Long employeeId, String isMod) {
        this.id = id;
        this.timestamp = timestamp;
        this.employeeId = employeeId;
        this.isMod = isMod;
    }

    public TimeStampDTO(TimeStampDTO timeStampDTO) {
    }

    public String getIsMod() {
        return isMod;
    }

    public void setIsMod(String isMod) {
        this.isMod = isMod;
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

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
