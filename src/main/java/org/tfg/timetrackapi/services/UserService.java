package org.tfg.timetrackapi.services;

import org.tfg.timetrackapi.dto.UserDTO;
import org.tfg.timetrackapi.entity.User;

public interface UserService {

    UserDTO save (UserDTO userDTO);

    UserDTO update (Long id, UserDTO userDTO);

    User getById(Long id);

    void delete(Long id);


}
