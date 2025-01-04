package com.example.ttcn2etest.service.user;

import com.example.ttcn2etest.model.dto.User1DTO;
import com.example.ttcn2etest.model.dto.UserDTO;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.request.user.*;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface UserService {
    List<UserDTO> getAllUser();

    UserDTO getByIdUser(Long id);

    UserDTO createUser(CreateUserRequest request);

    UserDTO updateUser(UpdateUserRequest request, Long id) throws ParseException;

    UserDTO deleteByIdUser(Long id);

    List<UserDTO> deleteAllIdUser(List<Long> ids);

    Page<User> filterUser(FilterUserRequest request, Date dateFrom, Date dateTo, Date dateOfBirthFrom, Date dateOfBirthTo);

    UserDTO getUserProfile();

    User1DTO getUserProfileAndService();

    UserDTO updateAvatar(UploadAvatarRequest request);

    UserDTO updateCustomer(UpdateCustomerRequest request);

    void changePassword(ChangePasswordRequest request);
    void forgotPassword(ForgotPasswordRequest request);

}
