package org.tfg.timetrackapi.controller;


import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.tfg.timetrackapi.dto.*;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.security.service.CustomUserDetails;
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


   /* @PostMapping("/add/{userId}")
    public ResponseEntity<TimeStamp> addTimeStamp(@PathVariable Long userId){

        User employee = userService.getById(userId);
        TimeStamp timeStamp = timeStampService.addTimeStamp(employee);
        return ResponseEntity.ok(timeStamp);
    }*/

    @PostMapping("/fichar")
    public ResponseEntity<?> fichar(@RequestBody FichajeRequest request) {
        try {
            User employee = userService.authenticateUser(request.getDni(), request.getPassword());
            TimeStamp timeEntry = timeStampService.addTimeStamp(employee);
            return ResponseEntity.ok(timeEntry);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @GetMapping("/employee/{employeeId}")
    public List<TimeStampDTO> getTimeStampsByEmployeeId(@PathVariable Long employeeId) {
        return timeStampService.getTimeStampsByEmployeeId(employeeId);
    }

    @GetMapping("/employee/{employeeId}/month")
    public ResponseEntity<List<TimeStampDTO>> getTimeStampsByEmployeeIdAndMonth(
            @PathVariable Long employeeId,
            @RequestParam int year,
            @RequestParam int month,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        boolean isAdmin = user.getRole().name().equals("ADMIN")|| user.getRole().name().equals("GUEST");
        boolean isSelf = user.getId().equals(employeeId);

        if (!isAdmin && !isSelf) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<TimeStampDTO> result = timeStampService.getTimeStampsByEmployeeIdAndMonth(employeeId, year, month);
        return ResponseEntity.ok(result);
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

            // Añadir mensaje de éxito
            response.put("message", "Fichaje creado correctamente.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Añadir mensaje de error
            response.put("error", "Error al crear el fichaje: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        timeStampService.deleteRecord(id);
        return ResponseEntity.ok("Record con el id " + id +  " eliminado con exito");
    }
    /*@GetMapping("/last3")
    public List<Last3Dto> getLastThreeTimestamps() {
        return timeStampService.getLastThreeTimestamps();
    }*/
}
