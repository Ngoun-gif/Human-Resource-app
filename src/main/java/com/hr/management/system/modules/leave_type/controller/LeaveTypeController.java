package com.hr.management.system.modules.leave_type.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hr.management.system.common.dto.PageResponse;
import com.hr.management.system.modules.leave_type.dto.LeaveTypeResponse;
import com.hr.management.system.modules.leave_type.dto.request.CreateLeaveTypeRequest;
import com.hr.management.system.modules.leave_type.dto.request.UpdateLeaveTypeRequest;
import com.hr.management.system.modules.leave_type.service.LeaveTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leave-types")
@RequiredArgsConstructor
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @PostMapping
    public LeaveTypeResponse create(@Valid @RequestBody CreateLeaveTypeRequest request) {
        return leaveTypeService.create(request);
    }

    @PutMapping("/{id}")
    public LeaveTypeResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLeaveTypeRequest request
    ) {
        return leaveTypeService.update(id, request);
    }

    @GetMapping("/{id}")
    public LeaveTypeResponse getById(@PathVariable Long id) {
        return leaveTypeService.getById(id);
    }

    @GetMapping
    public PageResponse<LeaveTypeResponse> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return leaveTypeService.getAll(search, page, size);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        leaveTypeService.delete(id);
        return "Leave type deleted successfully";
    }
}