package org.tfg.timetrackapi.controller;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.timetrackapi.dto.UserDTO;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody UserDTO userDTO){

        User userSaved = userService.save(userDTO);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userSaved);
    }




}
