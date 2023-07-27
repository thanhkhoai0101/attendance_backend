package com.khoai.attendance_backend.controller.group;

import com.khoai.attendance_backend.dto.response.ApiResponse;
import com.khoai.attendance_backend.dto.response.CountMember;
import com.khoai.attendance_backend.enums.TypeAttendance;
import com.khoai.attendance_backend.model.Attendance;
import com.khoai.attendance_backend.model.Group;
import com.khoai.attendance_backend.model.MemberGroup;
import com.khoai.attendance_backend.model.User;
import com.khoai.attendance_backend.repository.GroupRepository;
import com.khoai.attendance_backend.repository.MemberGroupRepository;
import com.khoai.attendance_backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("groups")
@Tag(name = "Group API")
public class GroupController {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MemberGroupRepository memberGroupRepository;

    public GroupController(GroupRepository groupRepository,
                           UserRepository userRepository,
                           MemberGroupRepository memberGroupRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.memberGroupRepository = memberGroupRepository;
    }

    @GetMapping("")
    public ApiResponse<List<Group>> listGroup(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ApiResponse.<List<Group>>builder()
                .data(groupRepository.findAllByCreator(user))
                .build();
    }

    @GetMapping("details/{id}")
    public ApiResponse<Group> getGroup(@PathVariable Long id){
        return ApiResponse.<Group>builder()
                .data(groupRepository.findById(id).get())
                .build();
    }

    @PostMapping("create")
    public ApiResponse<Group> create(@Valid @RequestBody String groupName){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (groupName == null){
            return  ApiResponse.<Group>builder()
                    .message("Tên nhóm không được để trống!")
                    .build();
        }
        Group group = new Group();
        group.setCreator(user);
        group.setName(groupName);
        return ApiResponse.<Group>builder()
                .data(groupRepository.save(group))
                .build();
    }

    @PostMapping("update")
    public ApiResponse<Group> update(@RequestBody Group group){
        Group result = groupRepository.findById(group.getId()).get();
        result = group;
        return ApiResponse.<Group>builder().data(groupRepository.save(result)).build();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        Group group = groupRepository.findById(id).get();
        group.setStatus(false);
        groupRepository.save(group);
    }

    @DeleteMapping("")
    public void deleteAllGroup(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        groupRepository.findAllByCreator(user).stream().filter(Group::getStatus).forEach(group -> {
            group.setStatus(false);
            groupRepository.save(group);
        });
    }

//    @GetMapping("list-all-members")
//    public ApiResponse<List<CountMember>> listAllMembers(){
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return ApiResponse.<List<CountMember>>builder()
//                .data(groupRepository.findAllMember(user))
//                .build();
//    }

    @PostMapping("upload-file/{groupId}")
    public ApiResponse<String> uploadExcelFile(@RequestParam("file") MultipartFile file, @PathVariable Long groupId){

        String SHEET = "Members";

        try{
            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<MemberGroup> memberGroups = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                MemberGroup memberGroup = new MemberGroup();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            memberGroup.setUser(userRepository.findById(Long.parseLong(currentCell.getStringCellValue())).get());
                            break;
                        default:
                            break;
                    }
                    if (memberGroup != null){
                        memberGroup.setGroup(groupRepository.findById(groupId).get());
                    }

                    cellIdx++;
                }

                memberGroups.add(memberGroup);
            }

            memberGroupRepository.saveAll(memberGroups);

            return ApiResponse.<String>builder()
                    .message("Thành công!").build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
