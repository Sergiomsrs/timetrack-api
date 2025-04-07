package org.tfg.timetrackapi.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.timetrackapi.dto.FichajeRequest;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.services.TimeStampService;
import org.tfg.timetrackapi.services.UserService;


@CrossOrigin("*")
@RestController
@RequestMapping("/apis/timestamp")
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
}
