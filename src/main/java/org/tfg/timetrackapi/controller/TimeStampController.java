package org.tfg.timetrackapi.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.services.TimeStampService;
import org.tfg.timetrackapi.services.UserService;

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
}
