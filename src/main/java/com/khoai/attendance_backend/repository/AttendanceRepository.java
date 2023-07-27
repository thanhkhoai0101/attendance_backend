package com.khoai.attendance_backend.repository;

import com.khoai.attendance_backend.model.Attendance;
import com.khoai.attendance_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {
    List<Attendance> findAllByMember(User user);


    List<Attendance> findAllByTypeAttendanceAndMember(String typeAttendance, User user);

    List<Attendance> findAllByTypeAttendanceAndTypeAttendanceId(String typeAttendance, Long typeAttendanceId);

    List<Attendance> findAllByTypeAttendance(String typeAttendance);

    Boolean existsAttendanceByTypeAttendanceAndTypeAttendanceIdAndMember(String typeAttendance, Long typeAttendanceId,User user);
}
