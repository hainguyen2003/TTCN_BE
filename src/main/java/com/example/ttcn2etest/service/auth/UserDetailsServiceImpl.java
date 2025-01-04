package com.example.ttcn2etest.service.auth;

import com.example.ttcn2etest.model.UserDetailsImpl;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        return UserDetailsImpl.builder()
                .userName(user.getUsername())
                .id(user.getId())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }
}
