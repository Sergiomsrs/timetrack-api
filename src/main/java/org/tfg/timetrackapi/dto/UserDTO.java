package org.tfg.timetrackapi.dto;

import jakarta.persistence.Column;
import org.tfg.timetrackapi.entity.Role;

import java.time.LocalDate;

public class UserDTO {

    private Long id;
    private String name;
    private String lastName;
    private String secondLastName;

    private String dni;
    private String password;

    private String email;

    private Role role;

    private LocalDate fechaAlta;

    private LocalDate fechaBaja;

    public UserDTO() {
    }

    public UserDTO(String name, String lastName, String secondLastName, String dni, String password, String email) {
        this.name = name;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.dni = dni;
        this.password = password;
        this.email = email;
    }

    public UserDTO(String name, String lastName, String secondLastName, String dni, String password, String email, Role role) {
        this.name = name;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.dni = dni;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public UserDTO(String name, String lastName, String dni, Role role) {
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.role = role;
    }

    public UserDTO(Long id, String name, String lastName, String dni, Role role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.role = role;
    }

    public UserDTO( String name, String lastName, String secondLastName, String dni, String password, String email, Role role, LocalDate fechaAlta, LocalDate fechaBaja) {

        this.name = name;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.dni = dni;
        this.password = password;
        this.email = email;
        this.role = role;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
}
