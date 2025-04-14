package org.tfg.timetrackapi.dto;

import java.time.LocalDateTime;

public class TimeStampDataDTO {

    private LocalDateTime timestamp;

    public TimeStampDataDTO() {
    }

    public TimeStampDataDTO(LocalDateTime timestamp) {

        this.timestamp = timestamp;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
