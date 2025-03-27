package org.tfg.timetrackapi.dto;

public class UserDTO {

    private String name;
    private String lastName;
    private String secondLastName;
    private int accessLevel;

    public UserDTO() {
    }

    public UserDTO(String name, String lastName, String secondLastName, int accessLevel) {
        this.name = name;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.accessLevel = accessLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}
