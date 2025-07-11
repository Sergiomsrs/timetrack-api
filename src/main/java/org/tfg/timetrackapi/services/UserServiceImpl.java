package org.tfg.timetrackapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.dto.UserDTO;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setSecondLastName(userDTO.getSecondLastName());
        user.setDni(userDTO.getDni());
        user.setEmail(userDTO.getEmail());
        user.setFechaAlta(userDTO.getFechaAlta());
        user.setFechaBaja(userDTO.getFechaBaja());

        // Encriptar la contraseña antes de guardarla
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(userDTO.getRole());


        User savedUser = userRepository.save(user);

        return new UserDTO(

                savedUser.getName(),
                savedUser.getLastName(),
                savedUser.getSecondLastName(),
                savedUser.getDni(),
                null,
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getFechaAlta(),
                savedUser.getFechaBaja()
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
        userToUpdate.setDni(userDTO.getDni());
        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setRole(userDTO.getRole());
        userToUpdate.setFechaAlta(userDTO.getFechaAlta());
        userToUpdate.setFechaBaja(userDTO.getFechaBaja());

       // validar si la contraseña ha sido modificada
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
           // Validar si la contraseña esta encriptada
            if (!passwordEncoder.matches(userDTO.getPassword(), userToUpdate.getPassword())) {
                String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
                userToUpdate.setPassword(encodedPassword);
            }
        }

        User updatedUser = userRepository.save(userToUpdate);

        return new UserDTO(
                updatedUser.getName(),
                updatedUser.getLastName(),
                updatedUser.getSecondLastName(),
                updatedUser.getDni(),
                updatedUser.getPassword(),
                updatedUser.getEmail(),
                updatedUser.getRole(),
                updatedUser.getFechaAlta(),
                updatedUser.getFechaBaja()
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
    public User authenticateUser(String dni, String rawPassword) {
        User user = userRepository.findByDni(dni)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return user;
    }

    @Override
    public List<User> findAll() {
       return userRepository.findAll();
    }

    @Override
    public List<User> findAllActive() {
        return userRepository.findAllActive();
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUsersByName(String name, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> findByDni(String dni) {
        return userRepository.findByDni(dni);
    }
}
