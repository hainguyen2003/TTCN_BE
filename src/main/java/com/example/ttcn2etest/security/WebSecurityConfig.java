package com.example.ttcn2etest.security;

import com.example.ttcn2etest.config.CorsConfigFilter;
import com.example.ttcn2etest.service.auth.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final UserDetailsService myUserDetailService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final CorsConfigFilter corsConfigFilter;

    @Autowired
    public WebSecurityConfig(UserDetailsService myUserDetailService, AuthEntryPointJwt unauthorizedHandler, JwtTokenProvider jwtTokenProvider, CorsConfigFilter corsConfigFilter) {
        this.myUserDetailService = myUserDetailService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.corsConfigFilter = corsConfigFilter;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtTokenProvider);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/client/**").permitAll()
                .requestMatchers("/user/auth/**").permitAll()
                .requestMatchers("/file/**").permitAll()
                .requestMatchers("/user/forgot/password").permitAll()
                .requestMatchers("/consulting/registration").permitAll()
                .requestMatchers("consulting/registration").permitAll()
                .requestMatchers("/course/registrationn").permitAll()
                .requestMatchers("course/registrationn").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/paymentVNPAY/vn-pay-callback/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/paymentVNPAY/vn-pay").permitAll()
                .requestMatchers("/order/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/order/{orderId}/update-status").authenticated()
                .requestMatchers("/classRoom/**").permitAll()
                .requestMatchers("/document/**").permitAll()
                .requestMatchers("/class-user/**").permitAll()
                .requestMatchers(request -> {
                    if (request.getMethod().equals(HttpMethod.GET.toString())) {
                        return new RegexRequestMatcher("/(document|news|admissions|slide|service|display|exam/schedule|...)/(all|\\d+)", null).matches(request);
                    }
                    return false;
                }).permitAll()
                .anyRequest().authenticated();
//                .anyRequest().permitAll();

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(corsConfigFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(unauthorizedHandler)
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeHttpRequests()
//                // Các endpoint công khai (không yêu cầu xác thực)
//                .requestMatchers("/auth/**", "/client/**", "/user/auth/**", "/file/**", "/user/forgot/password", "/consulting/registration").permitAll()
//                .requestMatchers(HttpMethod.GET, "/course/information/**").permitAll() // Công khai các GET request
//                .requestMatchers(HttpMethod.GET, "/admissions/all", "/admissions/{id}").permitAll() // Công khai GET cho admissions
//                .requestMatchers(HttpMethod.GET, "/document/**", "/news/**", "/slide/**", "/service/**", "/display/**", "/exam/schedule/**").permitAll() // Công khai GET khác
//                // Các endpoint cần xác thực
//                .requestMatchers("/admissions/**", "/course/information/**").authenticated()
//                // Các endpoint còn lại
//                .anyRequest().authenticated();
//
//        http.authenticationProvider(authenticationProvider());
//        http.addFilterBefore(corsConfigFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }


}
