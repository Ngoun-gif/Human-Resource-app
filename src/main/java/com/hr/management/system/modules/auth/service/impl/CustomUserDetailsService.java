package com.hr.management.system.modules.auth.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.hr.management.system.modules.user.entity.User;
import com.hr.management.system.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // ✅ ROLE authorities (ROLE_ADMIN)
        Set<SimpleGrantedAuthority> roleAuthorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());

        // ✅ PERMISSION authorities (EMPLOYEE_CREATE, ROLE_VIEW, ...)
        Set<SimpleGrantedAuthority> permissionAuthorities = user.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());

        // 🔥 merge both
        roleAuthorities.addAll(permissionAuthorities);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                true,
                roleAuthorities
        );
    }
}