package com.khoai.attendance_backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Size(max = 100)
    @Column(name = "start_time_attendance", length = 100)
    private String startTimeAttendance;

    @Size(max = 100)
    @Column(name = "end_time_attendance", length = 100)
    private String endTimeAttendance;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Size(max = 255)
    @Column(name = "location")
    private String location;

}
