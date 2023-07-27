package com.khoai.attendance_backend.repository;

import com.khoai.attendance_backend.model.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long>, JpaSpecificationExecutor<MemberGroup> {
}
