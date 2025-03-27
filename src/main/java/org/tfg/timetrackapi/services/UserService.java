package org.tfg.timetrackapi.services;

import org.tfg.timetrackapi.dto.UserDTO;
import org.tfg.timetrackapi.entity.User;

public interface UserService {

    User save (UserDTO userDTO);


}
