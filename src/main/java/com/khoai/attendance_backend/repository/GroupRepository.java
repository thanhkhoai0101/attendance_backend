package com.khoai.attendance_backend.repository;

import com.khoai.attendance_backend.dto.response.CountMember;
import com.khoai.attendance_backend.model.Group;
import com.khoai.attendance_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    List<Group> findAllByCreator(User user);

//    @Query("select distinct new com.khoai.attendance_backend.dto.response.CountMember(count(Attendance.member),member) " +
//            " from Group ,Lesson ,Attendance where Group = Lesson.group and Attendance.typeAttendanceId = Lesson.id and Attendance.typeAttendance like 'LESSON'" +
//            "and Group.creator =: creator and Group.status = true GROUP BY Attendance.member")
//    List<CountMember> findAllMember(@Param("creator") User creator);
}
