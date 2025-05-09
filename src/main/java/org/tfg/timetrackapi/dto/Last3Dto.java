package org.tfg.timetrackapi.dto;

public class Last3Dto {

    private Long id;
    private String timestampFormatted;
    private String dni;

    public Last3Dto(Long id, String timestampFormatted, String dni) {
        this.id = id;
        this.timestampFormatted = timestampFormatted;
        this.dni = dni;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimestampFormatted() {
        return timestampFormatted;
    }

    public void setTimestampFormatted(String timestampFormatted) {
        this.timestampFormatted = timestampFormatted;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
