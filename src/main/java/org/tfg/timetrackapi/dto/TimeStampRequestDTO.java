package org.tfg.timetrackapi.dto;

public class TimeStampRequestDTO {
    private Long employeeId;
    private String timestamp;

    private String isMod;

    public TimeStampRequestDTO() {
    }

    public String getIsMod() {
        return isMod;
    }

    public void setIsMod(String isMod) {
        this.isMod = isMod;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

