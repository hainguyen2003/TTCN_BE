package com.example.ttcn2etest.service.auth;

import com.example.ttcn2etest.model.dto.UserDTO;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DetailsUserNameService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Autowired
    public DetailsUserNameService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        Long userId = Long.parseLong(claims.getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return modelMapper.map(user, UserDTO.class);

    }
}
