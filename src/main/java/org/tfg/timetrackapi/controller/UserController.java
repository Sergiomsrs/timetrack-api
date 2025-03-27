package org.tfg.timetrackapi.controller;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public ResponseEntity<User> save(){
        User user = new User();
        user.setName("Juan");
        user.setLastName("Mendez");
        user.setSecondLastName("Navarro");
        user.setAccessLevel(2);

        User userSaved = userService.save(user);


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userSaved);
    }




}
