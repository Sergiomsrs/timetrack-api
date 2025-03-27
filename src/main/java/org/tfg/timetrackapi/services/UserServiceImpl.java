package org.tfg.timetrackapi.services;

import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.dto.UserDTO;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User save(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setSecondLastName(userDTO.getSecondLastName());
        user.setAccessLevel(userDTO.getAccessLevel());

        return userRepository.save(user);
    }
}
