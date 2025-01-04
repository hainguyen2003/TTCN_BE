package com.example.ttcn2etest.service.auth;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.UserDetailsImpl;
import com.example.ttcn2etest.model.etity.Role;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.news.NewsRepository;
import com.example.ttcn2etest.repository.order.OrderRepository;
import com.example.ttcn2etest.repository.role.RoleRepository;
import com.example.ttcn2etest.repository.service.ServiceRepository;
import com.example.ttcn2etest.repository.service.CustomServiceRepository;
import com.example.ttcn2etest.repository.news.CustomNewsRepository;
import com.example.ttcn2etest.repository.user.UserRepository;
import com.example.ttcn2etest.request.auth.LoginRequest;
import com.example.ttcn2etest.request.auth.RegisterRequest;
import com.example.ttcn2etest.response.DashBoardResponse;
import com.example.ttcn2etest.response.MonthlySaleResponse;
import com.example.ttcn2etest.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.time.*;

@Service
@Slf4j
public class AuthenServiceImpl implements AuthenService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final OrderRepository orderRepository;
    private final ServiceRepository serviceRepository;
    private final NewsRepository newsRepository;

    @Autowired
    public AuthenServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            OrderRepository orderRepository,
            ServiceRepository serviceRepository,
            NewsRepository newsRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.orderRepository = orderRepository;
        this.serviceRepository = serviceRepository;
        this.newsRepository = newsRepository;
    }

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateTokenWithAuthorities(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return new LoginResponse(jwt,
                    userDetails.getId(),
                    userDetails.getName(),
                    userDetails.getUsername(),
                    userDetails.getPhone(),
                    userDetails.getEmail(),
                    roles);
        }catch (BadCredentialsException e){
            throw new MyCustomException("Sai mật khẩu!");
        }catch (Exception e){
            throw new MyCustomException("Thông tin đăng nhập sai!");
        }


    }

    @Override
    public void registerUser(RegisterRequest signUpRequest) {
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
                .build();

        userRepository.saveAndFlush(user);
    }

    public DashBoardResponse getDashBoard(int year) {
        // Xác định khoảng thời gian bắt đầu và kết thúc của năm
        LocalDate startDate = Year.of(year).atDay(1);
        LocalDate endDate = Year.of(year).atDay(1).plusYears(1).minusDays(1);

        // Chuyển đổi LocalDate sang timestamp (long) để sử dụng trong truy vấn JPA
        long start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long end = endDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant().toEpochMilli();

        // Tạo đối tượng DashBoardResponse
        DashBoardResponse response = new DashBoardResponse();

        // Lấy danh sách doanh thu theo tháng
        List<MonthlySaleResponse> list = getMonthlySale(year);
        response.setListSale(list);

        // Đếm số lượng người dùng
        response.setCountUser((int) userRepository.count());

        // Tạo filter cho dịch vụ
        response.setCountService((int) serviceRepository.count());

        // Tạo filter cho bài viết (News)
        response.setCountNew((int) newsRepository.count());

        // Tạo filter cho đơn hàng (Orders)
        response.setCountOrder((int) orderRepository.count());

        // Tính tổng doanh thu từ danh sách MonthlySaleResponse
        int totalCost = list.stream().mapToInt(MonthlySaleResponse::getTotalCost).sum();
        response.setTotalCost(totalCost);

        return response;
    }


    private List<MonthlySaleResponse> getMonthlySale(int year) {
        // Khởi tạo danh sách chứa giá trị mặc định ban đầu cho các tháng từ tháng 1 đến tháng 12
        List<Integer> monthlyTotalList = new ArrayList<>(Arrays.asList(new Integer[12]));
        Collections.fill(monthlyTotalList, 0);
// Lấy kết quả từ cơ sở dữ liệu
        List<Object[]> results = orderRepository.getTotalCostByMonthInYear(year);
// Duyệt qua kết quả và cập nhật giá trị tương ứng cho các tháng trong danh sách
        for (Object[] result : results) {
            int month = ((Number) result[0]).intValue(); // Lấy tháng từ kết quả
            int totalCost = ((Number) result[2]).intValue(); // Lấy tổng số tiền từ kết quả
            monthlyTotalList.set(month - 1, totalCost); // Cập nhật giá trị cho tháng tương ứng
        }
// Tạo danh sách chứa các đối tượng MonthlySaleResponse từ danh sách tổng số tiền của các tháng
        List<MonthlySaleResponse> monthlyTotals = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int totalCost = monthlyTotalList.get(i); // Lấy giá trị từ danh sách tổng số tiền của các tháng
            monthlyTotals.add(new MonthlySaleResponse(totalCost, "Tháng " + (i + 1))); // Thêm vào danh sách MonthlySaleResponse
        }
        return monthlyTotals;
    }
}
