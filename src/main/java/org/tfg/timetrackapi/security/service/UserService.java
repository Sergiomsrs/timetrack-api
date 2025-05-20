package org.tfg.timetrackapi.security.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.UserRepository;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Registrar un nuevo usuario
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash de la contraseña
        return userRepository.save(user);
    }

    // Buscar usuario por DNI
    public Optional<User> findByDni(String dni) {
        return userRepository.findByDni(dni);
    }

    // Autenticar usuario (Comparar contraseña)
    public boolean authenticateUser(String dni, String rawPassword) {
        Optional<User> userOpt = userRepository.findByDni(dni);
        if (userOpt.isPresent()) {
            return passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
        }
        return false;
    }
}
