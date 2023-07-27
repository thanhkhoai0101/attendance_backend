package com.khoai.attendance_backend.model;

import com.khoai.attendance_backend.enums.StatusAttendance;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Size(max = 10)
    @NotNull
    @Column(name = "type_attendance", nullable = false, length = 10)
    private String typeAttendance;

    @NotNull
    @Column(name = "type_attendance_id", nullable = false)
    private Long typeAttendanceId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "time_attendance")
    private Instant timeAttendance;

    @Size(max = 255)
    @Column(name = "location")
    private String location;

    @Size(max = 255)
    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "status_attendance")
    private StatusAttendance statusAttendance = StatusAttendance.ABSENT;

}
