package com.khoai.attendance_backend.controller.lesson;

import com.khoai.attendance_backend.dto.request.LessonRequest;
import com.khoai.attendance_backend.dto.response.ApiResponse;
import com.khoai.attendance_backend.model.Group;
import com.khoai.attendance_backend.model.Lesson;
import com.khoai.attendance_backend.repository.GroupRepository;
import com.khoai.attendance_backend.repository.LessonRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("lessons")
@Tag(name = "Lesson API")
public class LessonController {

    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;

    public LessonController(LessonRepository lessonRepository, GroupRepository groupRepository) {
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
    }

    @GetMapping("{groupId}")
    public ApiResponse<List<Lesson>> listLesson(@PathVariable Long groupId){
        Group group = groupRepository.findById(groupId).get();
        return ApiResponse.<List<Lesson>>builder()
                .data(lessonRepository.findAllByGroup(group))
                .build();
    }

    @GetMapping("{id}")
    public ApiResponse<Lesson> getLesson(@PathVariable Long id){
        return ApiResponse.<Lesson>builder()
                .data(lessonRepository.findById(id).get())
                .build();
    }

    @PostMapping("create")
    public ApiResponse<Lesson> create(@Valid @RequestBody LessonRequest request){
        Lesson lesson = new Lesson();
        lesson.setGroup(groupRepository.findById(request.getGroupId()).get());
        lesson.setLocation(request.getLocation());
        lesson.setStartTimeAttendance(request.getStartTimeAttendance());
        lesson.setEndTimeAttendance(request.getEndTimeAttendance());

        return ApiResponse.<Lesson>builder()
                .data(lessonRepository.save(lesson))
                .build();
    }

    @PostMapping("update")
    public ApiResponse<Lesson> update (@RequestBody Lesson lesson){
        Lesson result = lessonRepository.findById(lesson.getId()).get();
        result=lesson;
        return  ApiResponse.<Lesson>builder()
                .data(lessonRepository.save(result))
                .build();
    }
}
