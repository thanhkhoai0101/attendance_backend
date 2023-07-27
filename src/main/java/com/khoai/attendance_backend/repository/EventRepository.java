package com.khoai.attendance_backend.repository;

import com.khoai.attendance_backend.dto.response.CountMember;
import com.khoai.attendance_backend.model.Event;
import com.khoai.attendance_backend.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends PagingAndSortingRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findAllByCreator(User user);

    Event findEventByNameAndCreator(String name, User user);

//    @Query("select distinct new com.khoai.attendance_backend.dto.response.CountMember(count(Attendance.member),member) " +
//            " from Event ,Attendance where Attendance.typeAttendanceId = Event.id and Attendance.typeAttendance like 'EVENT'" +
//            "and Event.creator =: creator and Event.status = true GROUP BY Attendance.member")
//    List<CountMember> findAllMember(@Param("creator") User creator);
}
