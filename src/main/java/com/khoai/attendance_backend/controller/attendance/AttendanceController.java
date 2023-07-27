package com.khoai.attendance_backend.controller.attendance;

import com.khoai.attendance_backend.dto.request.AttendanceRequest;
import com.khoai.attendance_backend.dto.request.TypeAttendanceRequest;
import com.khoai.attendance_backend.dto.response.ApiResponse;
import com.khoai.attendance_backend.dto.response.UserResponse;
import com.khoai.attendance_backend.enums.TypeAttendance;
import com.khoai.attendance_backend.model.Attendance;
import com.khoai.attendance_backend.model.User;
import com.khoai.attendance_backend.repository.AttendanceRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("attendances")
@Tag(name = "Attendance API")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;

    public AttendanceController(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("")
    public ApiResponse<List<Attendance>> listAttendances() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ApiResponse.<List<Attendance>>builder()
                .data(attendanceRepository.findAllByMember(user))
                .build();
    }

    @PostMapping("create")
    public ApiResponse<Attendance> create(@Valid @RequestBody AttendanceRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!attendanceRepository.existsAttendanceByTypeAttendanceAndTypeAttendanceIdAndMember(request.getTypeAttendance(), request.getTypeAttendanceId(), user)) {
            return ApiResponse.<Attendance>builder()
                    .message("Bạn đã điểm danh rồi!")
                    .build();
        }

        Attendance attendance = new Attendance();

        attendance.setTimeAttendance(request.getTimeAttendance());
        attendance.setTypeAttendance(request.getTypeAttendance());
        attendance.setTypeAttendanceId(request.getTypeAttendanceId());
        attendance.setLocation(attendance.getLocation());
        attendance.setMember(user);
        attendance.setDeviceId(request.getDeviceId());

        return ApiResponse.<Attendance>builder()
                .data(attendanceRepository.save(attendance))
                .build();
    }

    @GetMapping("get-all-attendance-by-type")
    public ApiResponse<List<Attendance>> getAllAttendanceByType(@Valid @RequestBody TypeAttendanceRequest request) {
        if (request.getTypeAttendanceId() == 0) {
            return ApiResponse.<List<Attendance>>builder()
                    .data(attendanceRepository.findAllByTypeAttendance(request.getTypeAttendance()))
                    .build();
        }
        return ApiResponse.<List<Attendance>>builder()
                .data(attendanceRepository.findAllByTypeAttendanceAndTypeAttendanceId(request.getTypeAttendance(), request.getTypeAttendanceId()))
                .build();
    }
}
