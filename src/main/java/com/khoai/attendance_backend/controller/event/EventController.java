package com.khoai.attendance_backend.controller.event;

import com.khoai.attendance_backend.dto.request.EventRequest;
import com.khoai.attendance_backend.dto.response.ApiResponse;
import com.khoai.attendance_backend.dto.response.CountMember;
import com.khoai.attendance_backend.dto.response.UserResponse;
import com.khoai.attendance_backend.enums.TypeAttendance;
import com.khoai.attendance_backend.model.Event;
import com.khoai.attendance_backend.model.User;
import com.khoai.attendance_backend.repository.AttendanceRepository;
import com.khoai.attendance_backend.repository.EventRepository;
import com.khoai.attendance_backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("events")
@Tag(name = "Event Attendance")
public class EventController {
    private final EventRepository eventRepository;
    private final AttendanceRepository attendanceRepository;

    public EventController(EventRepository eventRepository, AttendanceRepository attendanceRepository) {
        this.eventRepository = eventRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("")
    public ApiResponse<List<Event>> listEvent() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ApiResponse.<List<Event>>builder()
                .data(eventRepository.findAllByCreator(user).stream().filter(Event::getStatus).toList())
                .build();
    }

    @GetMapping("details/{id}")
    public ApiResponse<Event> getEvent(@PathVariable @RequestHeader("ID") Long id) {
        return ApiResponse.<Event>builder()
                .data(eventRepository.findById(id).get())
                .build();
    }

    @PostMapping("create")
    public ApiResponse<Event> create(@Valid @RequestBody EventRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Event event = eventRepository.findEventByNameAndCreator(request.getName(), user);
        if (event != null && event.getStatus()) {
            return ApiResponse.<Event>builder()
                    .message("Sự kiện đã tồn tại!")
                    .build();
        }
        if (request.getName() == null || request.getLocation() == null) {
            return ApiResponse.<Event>builder()
                    .message("Thông tin bị thiếu!")
                    .build();
        }
        event = new Event();
        event.setCreator(user);
        event.setName(request.getName());
        event.setLocation(request.getLocation());
        event.setStartTimeAttendance(request.getStartTimeAttendance());
        event.setEndTimeAttendance(request.getEndTimeAttendance());

        return ApiResponse.<Event>builder()
                .data(eventRepository.save(event))
                .build();
    }

    @PostMapping("update")
    public ApiResponse<Event> update(@RequestBody Event event) {
        Event result = eventRepository.findById(event.getId()).get();
        result = event;
        return ApiResponse.<Event>builder()
                .data(eventRepository.save(result))
                .build();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        Event event = eventRepository.findById(id).get();
        event.setStatus(false);

        eventRepository.save(event);
    }

    @DeleteMapping("")
    public void deleteAllEvent() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        eventRepository.findAllByCreator(user).stream().filter(Event::getStatus).forEach(event -> {
            event.setStatus(false);
            eventRepository.save(event);
        });
    }

//    @GetMapping("list-all-members")
//    public ApiResponse<List<CountMember>> listAllMembers() {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return ApiResponse.<List<CountMember>>builder()
//                .data(eventRepository.findAllMember(user))
//                .build();
//    }

    @GetMapping("list-members/{idEvent}")
    public ApiResponse<List<UserResponse>> listMembers(@PathVariable Long idEvent) {

        return ApiResponse.<List<UserResponse>>builder()
                .data(attendanceRepository.findAllByTypeAttendanceAndTypeAttendanceId(TypeAttendance.EVENT.toString(), idEvent).stream().map(attendance -> attendance.getMember().toUserResponse()).toList())
                .build();
    }
}
