package com.example.ttcn2etest.email;


import com.example.ttcn2etest.constant.ErrorCodeDefs;
import com.example.ttcn2etest.controller.BaseController;
import com.example.ttcn2etest.request.auth.RegisterRequest;
import com.example.ttcn2etest.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/auth")
public class UserRegisterAuthenController extends BaseController {
    @Autowired
    private RegisterAuthenService registerAuthenService;

    @PostMapping("/register")
    public ResponseEntity<?> userRegisterAuthenController(@Validated @RequestBody RegisterRequest request) {
        registerAuthenService.userRegisterAuthen(request);

        return buildItemResponse("Đăng ký thành công. Vui lòng kiểm tra email để xác thực!");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("email-verification") String token) {
        boolean verified = registerAuthenService.verifyUser(token);
        if (verified) {
            return buildItemResponse("Xác thực thành công!");
        } else {
            BaseResponse response = new BaseResponse();
            response.setFailed(ErrorCodeDefs.SERVER_ERROR, "Xác thực thất bại!");
            response.setSuccess(false);
            response.setStatusCode(500);
            return ResponseEntity.ok(response);
        }
    }
}
