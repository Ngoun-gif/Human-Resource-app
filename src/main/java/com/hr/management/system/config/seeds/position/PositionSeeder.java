package com.hr.management.system.config.seeds.position;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hr.management.system.modules.position.entity.Position;
import com.hr.management.system.modules.position.repository.PositionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PositionSeeder {

    private final PositionRepository positionRepository;

    public Position seedPosition(String code, String name, String description) {
        return positionRepository.findByCode(code).orElseGet(() -> {
            LocalDateTime now = LocalDateTime.now();

            Position position = Position.builder()
                    .code(code)
                    .name(name)
                    .description(description)
                    .status(true)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            System.out.println("✅ Position created: " + name);
            return positionRepository.save(position);
        });
    }
}