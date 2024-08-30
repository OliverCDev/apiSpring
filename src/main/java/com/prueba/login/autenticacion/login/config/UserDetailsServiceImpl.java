package com.prueba.login.autenticacion.login.config;

import java.util.HashSet;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;


import com.prueba.login.autenticacion.login.model.User;
import com.prueba.login.autenticacion.login.model.UserRole;
import com.prueba.login.autenticacion.login.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con ese nombre: " + username);
        }

        User user = userOptional.get();

        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        for (UserRole role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole().getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                grantedAuthorities);
    }

    public boolean authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Reutilizar la instancia de BCryptPasswordEncoder para comparar la contraseña
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;


    }
}
