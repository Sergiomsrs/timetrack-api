package org.tfg.timetrackapi.controller;


import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tfg.timetrackapi.dto.FichajeRequest;
import org.tfg.timetrackapi.dto.TimeStampDTO;
import org.tfg.timetrackapi.dto.TimeStampDataDTO;
import org.tfg.timetrackapi.dto.TimeStampRequestDTO;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.services.TimeStampService;
import org.tfg.timetrackapi.services.UserService;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/timestamp")
public class TimeStampController {

    private final TimeStampService timeStampService;
    private final UserService userService;

    public TimeStampController(TimeStampService timeStampService, UserService userService) {
        this.timeStampService = timeStampService;
        this.userService = userService;
    }


    @PostMapping("/add/{userId}")
    public ResponseEntity<TimeStamp> addTimeStamp(@PathVariable Long userId){

        User employee = userService.getById(userId);
        TimeStamp timeStamp = timeStampService.addTimeStamp(employee);
        return ResponseEntity.ok(timeStamp);

    }


    @PostMapping("/fichar")
    public ResponseEntity<TimeStamp> fichar(@RequestBody FichajeRequest request) {
        // Autenticar al usuario con DNI y PIN
        User employee = userService.authenticateUser(request.getDni(), request.getPassword());

        // Guardar el fichaje
        TimeStamp timeEntry = timeStampService.addTimeStamp(employee);

        // Retornar el fichaje en la respuesta
        return ResponseEntity.ok(timeEntry);
    }

    @GetMapping("/employee/{employeeId}")
    public List<TimeStampDTO> getTimeStampsByEmployeeId(@PathVariable Long employeeId) {
        return timeStampService.getTimeStampsByEmployeeId(employeeId);
    }

    @GetMapping("/employee/{employeeId}/month")
    public List<TimeStampDTO> getTimeStampsByEmployeeIdAndMonth(
            @PathVariable Long employeeId,
            @RequestParam int year,
            @RequestParam int month) {
        return timeStampService.getTimeStampsByEmployeeIdAndMonth(employeeId, year, month);
    }


    @PatchMapping("/{timeTsId}")
    public ResponseEntity<Map<String, Object>> updateTimestamp(
            @PathVariable Long timeTsId,
            @RequestBody TimeStampRequestDTO request) {

        try {
            LocalDateTime newTimestamp = LocalDateTime.parse(request.getTimestamp());

            timeStampService.editTimeStampWithData(timeTsId, newTimestamp);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Fichaje actualizado correctamente.");
            response.put("id", timeTsId);
            return ResponseEntity.ok(response);

        } catch (DateTimeParseException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Formato de fecha inválido: " + e.getParsedString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/timestamp")
    public ResponseEntity<Map<String, String>> createTimeStamp(@RequestBody TimeStampRequestDTO request) {
        Map<String, String> response = new HashMap<>();
        try {
            Long employeeId = request.getEmployeeId();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            LocalDateTime newTimestamp = LocalDateTime.parse(request.getTimestamp(), formatter);
            String isMod = request.getIsMod();


            // Llamar al servicio para crear el timestamp
            timeStampService.addTimeStampWithData(employeeId, newTimestamp, isMod);

            // Añadir mensaje de éxito al mapa
            response.put("message", "Fichaje creado correctamente.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Añadir mensaje de error al mapa
            response.put("error", "Error al crear el fichaje: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        timeStampService.deleteRecord(id);
        return ResponseEntity.ok("Record con el id " + id +  " eliminado con exito");
    }

}
