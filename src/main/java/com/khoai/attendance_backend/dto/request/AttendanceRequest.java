package com.khoai.attendance_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AttendanceRequest {
    private String typeAttendance;
    private Long typeAttendanceId;
    private Instant timeAttendance;
    private String location;
    private String deviceId;
}
