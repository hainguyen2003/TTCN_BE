package com.example.ttcn2etest.service.user;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.email.EmailService;
import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.User1DTO;
import com.example.ttcn2etest.model.dto.UserDTO;
import com.example.ttcn2etest.model.etity.Role;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.role.RoleRepository;
import com.example.ttcn2etest.repository.service.ServiceRepository;
import com.example.ttcn2etest.repository.user.CustomUserRepository;
import com.example.ttcn2etest.repository.user.UserRepository;
import com.example.ttcn2etest.request.user.*;
import com.example.ttcn2etest.utils.MyUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ServiceRepository serviceRepository;
    @Autowired
    private EmailService emailService;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ServiceRepository serviceRepository, PasswordEncoder encoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.serviceRepository = serviceRepository;
        this.encoder = encoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDTO> getAllUser() {
        return userRepository.findAll().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).toList();
    }

    @Override
    public UserDTO getByIdUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDTO.class);
        } else {
            throw new MyCustomException("ID người dùng không tồn tại trong hệ thống!");
        }
    }

    @Override
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        try {
            checkUserIsExistByName(request.getUsername(), null);
            checkServiceIsValid(request.getServices());
            checkRoleIsValid(request.getRoleId());
            Optional<Role> roleOptional = roleRepository.findById(request.getRoleId());
            User user = User.builder()
                    .name(request.getName())
                    .username(request.getUsername())
                    .dateOfBirth(MyUtils.convertDateFromString(request.getDateOfBirth(), DateTimeConstant.DATE_FORMAT))
                    .email(request.getEmail())
                    .isVerified(request.isVerified())
                    .password(encoder.encode(request.getPassword()))
                    .address(request.getAddress())
                    .isSuperAdmin(false)
                    .phone(request.getPhone())
                    .avatar(request.getAvatar())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            List<com.example.ttcn2etest.model.etity.Service> services = buildService(request.getServices());
            user.setServices(services);
            user.setRole(buildRole(roleOptional.get().getRoleId()));

            return modelMapper.map(userRepository.saveAndFlush(user), UserDTO.class);
        } catch (Exception ex) {
            throw new MyCustomException("Quá trình tạo người dùng không thành công ! " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public UserDTO updateUser(UpdateUserRequest request, Long id) {
        try {
            checkUserIsExistByName(request.getUsername(), id);
            checkRoleIsValid(request.getRoleId());
            checkServiceIsValid(request.getServices());
            validateUserExist(id);
            Optional<Role> roleOptional = roleRepository.findById(request.getRoleId());
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                List<com.example.ttcn2etest.model.etity.Service> services = buildService(request.getServices());
                User user = userOptional.get();
                user.setUsername(request.getUsername());
                user.setName(request.getName());
                user.setDateOfBirth(MyUtils.convertDateFromString(request.getDateOfBirth(), DateTimeConstant.DATE_FORMAT));
                user.setEmail(request.getEmail());
                user.setVerified(request.isVerified());
//                user.setPassword(encoder.encode(request.getPassword()));
                user.setPhone(request.getPhone());
                user.setAvatar(request.getAvatar());
                user.setAddress(request.getAddress());
                user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                user.setRole(buildRole(roleOptional.get().getRoleId()));
                user.setServices(services);
                return modelMapper.map(userRepository.saveAndFlush(user), UserDTO.class);
            }
        } catch (Exception ex) {
            throw new MyCustomException("Quá trình cập nhật thông tin người dùng không thành công ! " + ex.getMessage());
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật thông tin người dùng!");
    }


    @Override
    @Transactional
    public UserDTO deleteByIdUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Người dùng có id:" + id + "cần xóa không tồn tại trong hệ thống!");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return modelMapper.map(userOptional, UserDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa người dùng!");
    }

    @Override
    @Transactional
    public List<UserDTO> deleteAllIdUser(List<Long> ids) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : userRepository.findAllById(ids)) {
            userDTOS.add(modelMapper.map(user, UserDTO.class));
        }
        userRepository.deleteAllByIdInBatch(ids);
        return userDTOS;
    }

    @Override
    public Page<User> filterUser(FilterUserRequest request, Date dateFrom, Date dateTo, Date dateOfBirthFrom, Date dateOfBirthTo) {
        Specification<User> specification = CustomUserRepository.filterSpecification(dateFrom, dateTo, dateOfBirthFrom, dateOfBirthTo, request);
        return userRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
    }

    @Override
    public UserDTO getUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();

            User loggedUser = userRepository.getUserByUsername(loggedInUsername);

            if (loggedUser != null) {
                UserDTO userDTO = modelMapper.map(loggedUser, UserDTO.class);

                return userDTO;
            }
        } catch (Exception ex) {
            throw new MyCustomException("Không tìm thấy thông tin người dùng!");
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình lấy thông tin người dùng!");
    }

    @Override
    public User1DTO getUserProfileAndService() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();

            User loggedUser = userRepository.getUserByUsername(loggedInUsername);

            if (loggedUser != null) {
                User1DTO userDTO = modelMapper.map(loggedUser, User1DTO.class);

                return userDTO;
            }
        } catch (Exception ex) {
            throw new MyCustomException("Không tìm thấy thông tin khách hàng!");
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình lấy thông tin người dùng!");
    }

    @Override
    public UserDTO updateAvatar(UploadAvatarRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();

            User loggedUser = userRepository.getUserByUsername(loggedInUsername);


            if (loggedUser != null) {
                loggedUser.setAvatar(request.getAvatar());
                userRepository.saveAndFlush(loggedUser);

                UserDTO userDTO = modelMapper.map(loggedUser, UserDTO.class);

                return userDTO;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Không tìm thấy thông tin khách hàng!");
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật ảnh!");
    }

    @Override
    public UserDTO updateCustomer(UpdateCustomerRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();

            User loggedUser = userRepository.getUserByUsername(loggedInUsername);

            if (loggedUser != null) {
                loggedUser.setName(request.getName());
                loggedUser.setEmail(request.getEmail());
                loggedUser.setPhone(request.getPhone());
                loggedUser.setAddress(request.getAddress());
                loggedUser.setDateOfBirth(MyUtils.convertDateFromString(request.getDateOfBirth(), DateTimeConstant.DATE_FORMAT));
                userRepository.saveAndFlush(loggedUser);

                UserDTO userDTO = modelMapper.map(loggedUser, UserDTO.class);

                return userDTO;
            }
        } catch (Exception ex) {
            throw new MyCustomException("Không tìm thấy thông tin khách hàng!");
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật thông tin khách hàng!");
    }


    @Override
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        Optional<User> userOptional = userRepository.findByUsername(loggedInUsername);
        if(!userOptional.isPresent()){
            throw new MyCustomException("Người dùng không tồn tại!");
        }
        User user = userOptional.get();

        if(!encoder.matches(request.getOldPassword(), user.getPassword())){
            throw new MyCustomException("Mật khẩu cũ không chính xác!");
        }

        if(!isValidPassword(request.getNewPassword())){
            throw new MyCustomException("Mật khẩu mới không hợp lệ!");
        }

        if(!request.getConfirmNewPassword().equals(request.getNewPassword())){
            throw new MyCustomException("Mật khẩu không khớp nhau!");
        }

        if(encoder.matches(request.getNewPassword(), user.getPassword()))
            throw new MyCustomException("Mật khẩu mới phải khác mật khẩu cũ!");

        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.saveAndFlush(user);

    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null){
            throw new MyCustomException("Không tìm thấy người dùng ứng với địa chỉ email này!");
        }
        String newPassword = generateVerificationCode();
        user.setPassword(encoder.encode(newPassword));
        userRepository.saveAndFlush(user);

        String emailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Quên mật khẩu</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background-color: #fff;\n" +
                "            border-radius: 5px;\n" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "            margin: 0 auto;\n" +
                "            max-width: 600px;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #FB9400;\n" +
                "            border-radius: 5px 5px 0 0;\n" +
                "            color: #fff;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .code {\n" +
                "            background-color: #FFF4E5;\n" +
                "            border: 2px solid #FB9400;\n" +
                "            border-radius: 4px;\n" +
                "            color: #000000;\n" +
                "            display: inline-block;\n" +
                "            font-size: 18px;\n" +
                "            margin-top: 10px;\n" +
                "            padding: 10px 15px;\n" +
                "            margin: 10px auto;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            background-color: #f4f4f4;\n" +
                "            border-radius: 0 0 5px 5px;\n" +
                "            padding: 10px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h2>Quên mật khẩu</h2>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Chào bạn!</p>\n" +
                "            <p>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu EduStar của bạn.</p>\n" +
                "            <p>Đây là mã xác nhận của bạn:</p>\n" +
                "            <div class=\"code\">"+newPassword+"</div>\n" +
                "            <p>Nhập mã để đăng nhập vào tài khoản EduStar của bạn !</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>Trân trọng,</p>\n" +
                "            <p>EduStar</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";


        emailService.sendEmailHtml(user.getEmail(), newPassword + " là mã khôi phục tài khoản EduStar của bạn !\n",emailContent);

    }



    public static String generateVerificationCode(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public static boolean isValidPassword(String password){
        if (password.length() < 6){
            throw new MyCustomException("Mật khẩu phải có ít nhất 6 ký tự!");
        }
        if(!password.matches(".*[A-Z].*")){
            throw new MyCustomException("Mật khẩu phải chưa ít nhất một chữ hoa!");
        }
        if(!password.matches(".*[a-z].*")){
            throw new MyCustomException("Mật khẩu phải chưa ít nhất một chữ thường!");
        }
        if(!password.matches(".*\\d.*")){
            throw new MyCustomException("Mật khẩu phải chứa ít nhất một chữ số.");
        }
        if(!password.matches(".*[@#$%^&+=].*")){
            throw new MyCustomException("Mật khẩu phải chứa ít nhất một ký tự đặc biệt.");
        }
        return true;
    }


    private Role buildRole(String roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role không tồn tại!"));
    }

    private void checkRoleIsValid(String roleId) {
        if (roleId == null)
            return;
        Role role = buildRole(roleId);
        if (role == null) {
            throw new MyCustomException("Vai trò không tồn tại!");
        }
    }

    private List<com.example.ttcn2etest.model.etity.Service> buildService(List<Long> serviceIds) {
        return serviceRepository.findAllById(serviceIds);
    }

    private void checkServiceIsValid(List<Long> serviceIds) {
        List<com.example.ttcn2etest.model.etity.Service> services = buildService(serviceIds);

        if (CollectionUtils.isEmpty(services)) {
            throw new MyCustomException("Dịch vụ học không tồn tại!");
        }
        List<Long> listIdExists = services.stream().map(com.example.ttcn2etest.model.etity.Service::getId).toList();
        List<Long> idNotExists = serviceIds.stream().filter(s -> !listIdExists.contains(s)).toList();

        if (!idNotExists.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Trong danh sách mã dịch vụ có mã không tồn tại trên hệ thống: ");
            for (Long id : idNotExists) {
                errorMessage.append(id).append(", ");
            }
            throw new MyCustomException(errorMessage.toString());
        }
    }

    private void checkUserIsExistByName(String name, Long id) {
        if (id == null && userRepository.existsAllByUsername(name))
            throw new MyCustomException("Tên đăng nhập đã tồn tại!");
        if (id != null && userRepository.existsAllByUsernameAndIdNot(name, id))
            throw new MyCustomException("Tên đăng nhập đã tồn tại!");
    }

    private void validateUserExist(Long id) {
        boolean isExist = userRepository.existsById(id);
        if (!isExist) {
            throw new MyCustomException("Người dùng không tồn tại trên hệ thống!");
        }
    }

    private List<Role> buildRole(List<String> roleIds) {
        return CollectionUtils.isEmpty(roleIds) ? new ArrayList<>() : roleRepository.findAllById(roleIds);
    }


}
