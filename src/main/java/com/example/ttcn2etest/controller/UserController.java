package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.model.dto.User1DTO;
import com.example.ttcn2etest.model.dto.UserDTO;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.request.user.*;
import com.example.ttcn2etest.service.user.UserService;
import com.example.ttcn2etest.utils.MyUtils;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllUser() {
        try {
            List<UserDTO> response = userService.getAllUser();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        UserDTO response = userService.getByIdUser(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> creatUser(@Valid @RequestBody CreateUserRequest request) {
        UserDTO response = userService.createUser(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> updateUser(@Validated @RequestBody UpdateUserRequest request,
                                 @PathVariable("id") Long id) throws ParseException {
        UserDTO response = userService.updateUser(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        UserDTO response = userService.deleteByIdUser(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        try {
            List<UserDTO> response = userService.deleteAllIdUser(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<?> filterUser(@Validated @RequestBody FilterUserRequest request) throws ParseException {
        Page<User> userPage = userService.filterUser(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateOfBirthFrom()) ? MyUtils.convertDateFromString(request.getDateOfBirthFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateOfBirthTo()) ? MyUtils.convertDateFromString(request.getDateOfBirthTo(), DateTimeConstant.DATE_FORMAT) : null
        );
        List<UserDTO> userDTOS = userPage.getContent().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).toList();
        return buildListItemResponse(userDTOS, userPage.getTotalElements());
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(){
        UserDTO userDTO = userService.getUserProfile();
        return buildItemResponse(userDTO);
    }

    @GetMapping("/profile/service")
    public ResponseEntity<?> getUserProfileAndService(){
        User1DTO userDTO = userService.getUserProfileAndService();
        return buildItemResponse(userDTO);
    }

    @PutMapping("/update/avatar")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<?> updateAvatar(
            @Valid @RequestBody UploadAvatarRequest request){
        UserDTO response = userService.updateAvatar(request);
        return buildItemResponse(response);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public  ResponseEntity<?> updateCustomer(@Validated @RequestBody UpdateCustomerRequest request){
        UserDTO response = userService.updateCustomer(request);
        return buildItemResponse(response);
    }

    @PostMapping("/change/password")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','ADMIN','STAFF')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request){
        userService.changePassword(request);
        return buildItemResponse("Mật khẩu đã được thay đổi thành công!");
    }

    @PostMapping("/forgot/password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){
        userService.forgotPassword(request);
        return buildItemResponse("Quá trình thực hiện chức năng quên mật khẩu thành công!");
    }
}
