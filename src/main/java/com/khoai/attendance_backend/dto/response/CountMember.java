package com.khoai.attendance_backend.dto.response;

import com.khoai.attendance_backend.model.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CountMember {
    private Long count;
    private User user;

    public CountMember(Long count, User user){
        this.count = count;
        this.user = user;
    }

}
