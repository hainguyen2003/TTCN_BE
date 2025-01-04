package com.example.ttcn2etest.email;

import com.example.ttcn2etest.constant.ErrorCodeDefs;
import com.example.ttcn2etest.exception.JwtTokenInvalid;
import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.etity.Role;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.role.RoleRepository;
import com.example.ttcn2etest.repository.user.UserRepository;
import com.example.ttcn2etest.request.auth.RegisterRequest;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class RegisterAuthenService {

    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Value("${jwt.jwtSecret}")
    private String jwtSecret;
    @Value("${jwt.jwtExpirationMs}")
    private Long jwtExpirationMs;

    public RegisterAuthenService(RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void userRegisterAuthen(RegisterRequest signUpRequest) {
        if (userRepository.existsAllByUsername(signUpRequest.getUsername())) {
            throw new MyCustomException("Tên tài khoản đã tồn tại!");
        }
        if (userRepository.existsAllByEmail(signUpRequest.getEmail())) {
            throw new MyCustomException("Email đã tồn tại trong hệ thống!");
        }
        Role customerRole = roleRepository.findByRoleId("CUSTOMER");
        if (customerRole == null) {
            customerRole = new Role();
            customerRole.setRoleId("CUSTOMER");
            customerRole = roleRepository.save(customerRole);
        }
        User user = User.builder()
                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .isSuperAdmin(false)
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .phone(signUpRequest.getPhone())
                .role(customerRole)
                .isVerified(false)
                .build();

        userRepository.saveAndFlush(user);

        String verificationToken = generateVerificationToken(user);


        String emailContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body {" +
                "    font-family: Arial, sans-serif;" +
                "    text-align: center;" +
                "    margin: 0;" +
                "    padding: 0;" +
                "}" +
                ".container {" +
                "    max-width: 700px;" +
                "    display: flex;" +
                "    margin: 0 auto;" +
                "    justify-content: center;" +
                "    align-items: center;" +
                "    background-color: #f5f5f5;" +
                "}" +
                ".content {" +
                "    text-align: center;" +
                "    margin: 0 auto;" +
                "}" +
                ".button {" +
                "    background-color: #FB9400;" +
                "    border: none;" +
                "    color: white;" +
                "    padding: 15px 32px;" +
                "    text-align: center;" +
                "    text-decoration: none;" +
                "    display: inline-block;" +
                "    font-size: 16px;" +
                "    margin: 4px 2px;" +
                "    cursor: pointer;" +
                "    border-radius: 8px;" +
                "    transition: background-color 0.3s;" +
                "}" +
                ".button:hover {" +
                "    background-color: #E56A00;" +
                "    cursor: pointer;" +
                "}" +
                "h1 {" +
                "    color: #333;" +
                "}" +
                "p {" +
                "    color: #555;" +
                "    font-size: 15px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='content'>" +
                "<h1>Xác Minh Email Của Bạn!</h1>" +
                "<p>Email &#10092; " + signUpRequest.getEmail() + " &#10093; đã đăng ký tài khoản tại EduStar!</p>" +
                "<p><a href='" + signUpRequest.getLink() + "?email-verification=" + verificationToken + "' style='text-decoration: none;'>" +
                "<button class='button'>Xác Minh Email</button>" +
                "</a></p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        emailService.sendEmailHtml(user.getEmail(), "Xác thực tài khoản!", emailContent);
    }

    private String generateVerificationToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(user.getId().toString())
//                .claim("username", user.getUsername())
//                .claim("email", user.getEmail())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }

    public boolean verifyUser(String token) {
        if (validateJwtToken(token)) {
            User user = decodeToken(token);
            if (user != null && !user.isVerified()) {
                    user.setVerified(true);
                    userRepository.saveAndFlush(user);
                    return true;
                }
            }
            return false;
        }

    public User decodeToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.parseLong(claims.getSubject());
            Optional<User> userOptional = userRepository.findById(userId);
            return userOptional.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        throw new JwtTokenInvalid(ErrorCodeDefs.getErrMsg(ErrorCodeDefs.TOKEN_INVALID));
    }


}
