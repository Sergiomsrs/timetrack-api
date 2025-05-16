package org.tfg.timetrackapi.controller;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.timetrackapi.dto.AbsenceDTO;
import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;
import org.tfg.timetrackapi.services.AbsenceNotificationService;
import org.tfg.timetrackapi.services.EmployeeScheduleService;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
public class EmployeeScheduleController {

    private final EmployeeScheduleService employeeScheduleService;
    private final AbsenceNotificationService absenceNotificationService;


    public EmployeeScheduleController(EmployeeScheduleService employeeScheduleService, AbsenceNotificationService absenceNotificationService) {
        this.employeeScheduleService = employeeScheduleService;
        this.absenceNotificationService = absenceNotificationService;
    }

    @PostMapping
    public ResponseEntity<String> guardarHorario(@RequestBody EmployeeScheduleDTO dto) {
        employeeScheduleService.guardarHorario(dto);
        return ResponseEntity.ok("Horario guardado correctamente");
    }

    @PostMapping("/verificar")
    public ResponseEntity<String> verificarFichajes() {
        employeeScheduleService.verificarFichajes();
        return ResponseEntity.ok("Verificación completada");
    }

    @GetMapping("/last10")
    public List<AbsenceDTO> getLast10Schedules() {
        return absenceNotificationService.getLast10SchedulesDTO();
    }

    @GetMapping("/user/{userId}")
    public List<EmployeeScheduleDTO> getSchedulesByUserId(@PathVariable Long userId) {
        return employeeScheduleService.getSchedulesByUserId(userId);
    }

    @PutMapping("/horarios/all")
    public ResponseEntity<List<EmployeeScheduleDTO>> updateAll(@RequestBody List<EmployeeScheduleDTO> dtoList) {
        List<EmployeeScheduleDTO> updated = employeeScheduleService.updateAll(dtoList);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/default/{dni}")
    public ResponseEntity<String> guardarHorarioPorDefecto(@PathVariable String dni) {
        employeeScheduleService.guardarHorarioPorDefecto(dni);
        return ResponseEntity.ok("Horario por defecto guardado");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            employeeScheduleService.delete(id);
            return ResponseEntity.ok("Registro eliminado correctamente.");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró ningún registro con ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el registro con ID: " + id);
        }
    }





}
