package org.tfg.timetrackapi.security.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.repository.UserRepository;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        System.out.println("Intentando cargar usuario con DNI: " + dni);
        return userRepository.findByDni(dni)
                .map(user -> {
                    System.out.println("Usuario encontrado: " + user.getDni());
                    return new CustomUserDetails(user); // En lugar de crear un User de Spring Security
                })
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado con DNI: " + dni);
                    return new UsernameNotFoundException("Usuario no encontrado con DNI: " + dni);
                });
    }
}

