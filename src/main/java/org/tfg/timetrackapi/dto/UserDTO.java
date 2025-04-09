package org.tfg.timetrackapi.dto;

public class UserDTO {

    private String name;
    private String lastName;
    private String secondLastName;
    private int accessLevel;

    private String dni;
    private String password;

    private String email;

    public UserDTO() {
    }

    public UserDTO(String name, String lastName, String secondLastName, int accessLevel, String dni, String password, String email) {
        this.name = name;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.accessLevel = accessLevel;
        this.dni = dni;
        this.password = password;
        this.email = email;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
