package com.hr.management.system.config.seeds.leave_type;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hr.management.system.modules.leave_type.entity.LeaveType;
import com.hr.management.system.modules.leave_type.repository.LeaveTypeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LeaveTypeSeeder {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveType seedLeaveType(String name, String description) {
        return leaveTypeRepository.findByName(name).orElseGet(() -> {
            LocalDateTime now = LocalDateTime.now();

            LeaveType leaveType = LeaveType.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            System.out.println("✅ LeaveType created: " + name);
            return leaveTypeRepository.save(leaveType);
        });
    }
}