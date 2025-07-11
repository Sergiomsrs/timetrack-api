package org.tfg.timetrackapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tfg.timetrackapi.dto.UserDTO;
import org.tfg.timetrackapi.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByDni(String dni);
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
    // Método para obtener solo los empleados activos
    @Query("SELECT u FROM User u WHERE u.fechaAlta <= CURRENT_DATE AND (u.fechaBaja IS NULL OR u.fechaBaja >= CURRENT_DATE)")
    List<User> findAllActive();

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.fechaAlta <= CURRENT_DATE AND (u.fechaBaja IS NULL OR u.fechaBaja >= CURRENT_DATE)")
    Optional<User> findActiveById(@Param("id") Long id);


}
