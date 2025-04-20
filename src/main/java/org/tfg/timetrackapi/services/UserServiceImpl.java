package org.tfg.timetrackapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.dto.UserDTO;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.UserRepository;

import java.util.List;


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
        user.setDni(userDTO.getDni());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());

        User savedUser = userRepository.save(user);

        return new UserDTO(
                savedUser.getName(),
                savedUser.getLastName(),
                savedUser.getSecondLastName(),
                savedUser.getAccessLevel(),
                savedUser.getDni(),
                savedUser.getPassword(),
                savedUser.getEmail()
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
        userToUpdate.setDni(userDTO.getDni());
        userToUpdate.setPassword(userDTO.getPassword());
        userToUpdate.setEmail(userDTO.getEmail());

        User updatedUser = userRepository.save(userToUpdate);

        return new UserDTO(
                updatedUser.getName(),
                updatedUser.getLastName(),
                updatedUser.getSecondLastName(),
                updatedUser.getAccessLevel(),
                updatedUser.getDni(),
                updatedUser.getPassword(),
                updatedUser.getEmail()
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

    @Override
    public User authenticateUser(String dni, String password) {
        User user = userRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con DNI: " + dni));

        // Verificar si el PIN es correcto
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("PIN incorrecto para el DNI: " + dni);
        }

        return user;
    }

    @Override
    public List<User> findAll() {
       return userRepository.findAll();
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUsersByName(String name, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
