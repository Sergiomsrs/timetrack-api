package org.tfg.timetrackapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.tfg.timetrackapi.dto.UserDTO;
import org.tfg.timetrackapi.entity.User;

import java.util.List;

public interface UserService {

    UserDTO save (UserDTO userDTO);

    UserDTO update (Long id, UserDTO userDTO);

    User getById(Long id);

    void delete(Long id);

    User authenticateUser (String dni, String password);

    List<User> findAll();

    Page<User> getUsers(Pageable pageable);
    Page<User> searchUsersByName(String name, Pageable pageable);


}
