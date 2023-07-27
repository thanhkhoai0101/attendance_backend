package com.khoai.attendance_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRequest {
    private String name;
    private String startTimeAttendance;
    private String endTimeAttendance;
    private String location;
}
