package com.khoai.attendance_backend.controller.user;

import com.khoai.attendance_backend.dto.response.ApiResponse;
import com.khoai.attendance_backend.dto.request.RegisterRequest;
import com.khoai.attendance_backend.dto.response.UserResponse;
import com.khoai.attendance_backend.model.User;
import com.khoai.attendance_backend.repository.LoginSessionRepository;
import com.khoai.attendance_backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
@Tag(name = "User Controller")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()) == null) {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setFullName(request.getFullName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            return ApiResponse.<UserResponse>builder()
                    .data(userRepository.save(user).toUserResponse())
                    .build();
        }

        return ApiResponse.<UserResponse>builder()
                .message("Tên tài khoản đã tồn tại!")
                .build();
    }

    @GetMapping("profile")
    public ApiResponse<UserResponse> profile(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println("ahihi " + user.getUsername());
        return ApiResponse.<UserResponse>builder()
                .data(user.toUserResponse())
                .build();
    }

    @PostMapping("update")
    public ApiResponse<UserResponse> update(@Valid @RequestBody User user){
        User res = userRepository.findById(user.getId()).get();
        res = user;
        res.setPassword(passwordEncoder.encode(res.getPassword()));
        return ApiResponse.<UserResponse>builder()
                .data(userRepository.save(res).toUserResponse())
                .build();
    }

}
