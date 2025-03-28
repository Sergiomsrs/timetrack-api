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
    public UserDTO save(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setSecondLastName(userDTO.getSecondLastName());
        user.setAccessLevel(userDTO.getAccessLevel());

        User savedUser = userRepository.save(user);

        return new UserDTO(
                savedUser.getName(),
                savedUser.getLastName(),
                savedUser.getSecondLastName(),
                savedUser.getAccessLevel()
        );
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTO) {

        User userToUpdate = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuario no encontrado con ID: " + id)
        );

        userToUpdate.setName(userDTO.getName());
        userToUpdate.setLastName(userDTO.getLastName());
        userToUpdate.setSecondLastName(userDTO.getSecondLastName());
        userToUpdate.setAccessLevel(userDTO.getAccessLevel());

        User updatedUser = userRepository.save(userToUpdate);

        return new UserDTO(
                updatedUser.getName(),
                updatedUser.getLastName(),
                updatedUser.getSecondLastName(),
                updatedUser.getAccessLevel()
        );
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuario no encontrado con ID: " + id)
        );
    }

    @Override
    public void delete(Long id) {

        if( !userRepository.existsById(id) ){
            throw new RuntimeException("Usuario no encontrado");
        }

        userRepository.deleteById(id);
    }
}
