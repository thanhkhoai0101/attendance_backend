package com.khoai.attendance_backend.controller.auth;

import com.khoai.attendance_backend.dto.response.ApiResponse;
import com.khoai.attendance_backend.dto.request.LoginRequest;
import com.khoai.attendance_backend.model.LoginSession;
import com.khoai.attendance_backend.model.User;
import com.khoai.attendance_backend.repository.LoginSessionRepository;
import com.khoai.attendance_backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("auth/")
@Tag(name = "Authentication")
public class AuthController {
    private final LoginSessionRepository loginSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthController(LoginSessionRepository loginSessionRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.loginSessionRepository = loginSessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("login")
    public ApiResponse<LoginSession> login(@Valid @RequestBody LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername());
        if(user != null){
            String token = UUID.randomUUID().toString();
            LoginSession loginSession = new LoginSession();

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())){
                loginSession.setUser(user);
                loginSession.setToken(token);

                return ApiResponse.<LoginSession>builder()
                        .data(loginSessionRepository.save(loginSession))
                        .build();
            }
            return ApiResponse.<LoginSession>builder()
                    .code("500")
                    .message("Mật khẩu không chính xác!")
                    .build();
        }
        return ApiResponse.<LoginSession>builder()
                .code("404")
                .message("Tài khoản không tồn tại")
                .build();
    }
}
