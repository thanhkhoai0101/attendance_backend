package com.khoai.attendance_backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Builder
public class UserResponse {
    private String fullName;
    private String username;
    private Instant createAt;
}
