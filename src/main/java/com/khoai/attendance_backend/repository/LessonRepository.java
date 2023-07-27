package com.khoai.attendance_backend.repository;

import com.khoai.attendance_backend.model.Group;
import com.khoai.attendance_backend.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {

    List<Lesson> findAllByGroup(Group group);
}
