package com.hr.management.system.config.seed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hr.management.system.modules.department.entity.Department;
import com.hr.management.system.modules.department.repository.DepartmentRepository;
import com.hr.management.system.modules.employee.entity.Employee;
import com.hr.management.system.modules.employee.enums.EmployeeGender;
import com.hr.management.system.modules.employee.enums.EmployeeStatus;
import com.hr.management.system.modules.employee.repository.EmployeeRepository;
import com.hr.management.system.modules.employee_type.entity.EmployeeType;
import com.hr.management.system.modules.employee_type.repository.EmployeeTypeRepository;
import com.hr.management.system.modules.leave_type.entity.LeaveType;
import com.hr.management.system.modules.leave_type.repository.LeaveTypeRepository;
import com.hr.management.system.modules.position.entity.Position;
import com.hr.management.system.modules.position.repository.PositionRepository;
import com.hr.management.system.modules.role.entity.Role;
import com.hr.management.system.modules.role.repository.RoleRepository;
import com.hr.management.system.modules.user.entity.User;
import com.hr.management.system.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // 1. Roles
        Role adminRole = seedRole("ADMIN", "System Administrator");
        seedRole("HR", "Human Resource");

        // 2. Admin user
        seedAdminUser(adminRole);

        // 3. Departments
        Department hrDepartment = seedDepartment("HR", "Human Resources");
        Department itDepartment = seedDepartment("IT", "Information Technology");
        Department financeDepartment = seedDepartment("FINANCE", "Finance Department");

        // 4. Positions
        Position managerPosition = seedPosition("MGR", "Manager", "Management role");
        Position officerPosition = seedPosition("OFF", "Officer", "Operational role");
        Position staffPosition = seedPosition("STAFF", "Staff", "General staff role");

        // 5. Employee Types
        EmployeeType fullTimeType = seedEmployeeType("FULL_TIME", "Full Time Employee");
        EmployeeType internType = seedEmployeeType("INTERN", "Intern");

        // 6. Leave Types
        seedLeaveType("ANNUAL", "Annual Leave");
        seedLeaveType("SICK", "Sick Leave");
        seedLeaveType("UNPAID", "Unpaid Leave");
        seedLeaveType("MATERNITY", "Maternity Leave");

        // 7. Employees
        seedEmployee(
                "EMP001",
                "Sok",
                "Dara",
                "sok.dara@hr.local",
                "012111111",
                LocalDate.of(2024, 1, 10),
                EmployeeGender.MALE,
                EmployeeStatus.ACTIVE,
                hrDepartment,
                officerPosition,
                fullTimeType
        );

        seedEmployee(
                "EMP002",
                "Srey",
                "Pich",
                "srey.pich@hr.local",
                "012222222",
                LocalDate.of(2024, 3, 5),
                EmployeeGender.FEMALE,
                EmployeeStatus.ACTIVE,
                itDepartment,
                staffPosition,
                fullTimeType
        );

        seedEmployee(
                "EMP003",
                "Nary",
                "Chan",
                "nary.chan@hr.local",
                "012333333",
                LocalDate.of(2025, 6, 1),
                EmployeeGender.FEMALE,
                EmployeeStatus.ACTIVE,
                financeDepartment,
                managerPosition,
                internType
        );

        System.out.println("✅ Base data seeded");
    }

    // =========================
    // ROLE
    // =========================
    private Role seedRole(String name, String description) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = Role.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            System.out.println("✅ Role created: " + name);
            return roleRepository.save(role);
        });
    }

    // =========================
    // ADMIN USER
    // =========================
    private void seedAdminUser(Role adminRole) {

        userRepository.findByUsername("admin").ifPresentOrElse(user -> {

            boolean needUpdate = false;
            LocalDateTime now = LocalDateTime.now();

            if (user.getEmail() == null) {
                user.setEmail("admin@hr.local");
                needUpdate = true;
            }

            if (user.getFirstName() == null) {
                user.setFirstName("System");
                needUpdate = true;
            }

            if (user.getLastName() == null) {
                user.setLastName("Administrator");
                needUpdate = true;
            }

            if (user.getPhone() == null) {
                user.setPhone("000000000");
                needUpdate = true;
            }

            if (user.getCreatedBy() == null) {
                user.setCreatedBy("SYSTEM");
                needUpdate = true;
            }

            if (user.getUpdatedBy() == null) {
                user.setUpdatedBy("SYSTEM");
                needUpdate = true;
            }

            if (user.getCreatedAt() == null) {
                user.setCreatedAt(now);
                needUpdate = true;
            }

            if (user.getUpdatedAt() == null) {
                user.setUpdatedAt(now);
                needUpdate = true;
            }

            if (!user.getRoles().contains(adminRole)) {
                user.getRoles().add(adminRole);
                needUpdate = true;
            }

            if (needUpdate) {
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
                System.out.println("🔄 Admin user updated");
            } else {
                System.out.println("ℹ️ Admin user already exists");
            }

        }, () -> {

            LocalDateTime now = LocalDateTime.now();

            User adminUser = User.builder()
                    .username("admin")
                    .email("admin@hr.local")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("System")
                    .lastName("Administrator")
                    .phone("000000000")
                    .enabled(true)
                    .createdAt(now)
                    .updatedAt(now)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(adminUser);

            System.out.println("✅ Admin user created: admin / admin123");
        });
    }

    // =========================
    // DEPARTMENT
    // =========================
    private Department seedDepartment(String name, String description) {
        return departmentRepository.findByName(name).orElseGet(() -> {
            Department dept = Department.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            System.out.println("✅ Department created: " + name);
            return departmentRepository.save(dept);
        });
    }

    // =========================
    // POSITION
    // =========================
    private Position seedPosition(String code, String name, String description) {
        return positionRepository.findByCode(code).orElseGet(() -> {
            Position pos = Position.builder()
                    .code(code)
                    .name(name)
                    .description(description)
                    .status(true)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            System.out.println("✅ Position created: " + name);
            return positionRepository.save(pos);
        });
    }

    // =========================
    // EMPLOYEE TYPE
    // =========================
    private EmployeeType seedEmployeeType(String name, String description) {
        return employeeTypeRepository.findByName(name).orElseGet(() -> {
            EmployeeType employeeType = EmployeeType.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            System.out.println("✅ EmployeeType created: " + name);
            return employeeTypeRepository.save(employeeType);
        });
    }

    // =========================
    // LEAVE TYPE
    // =========================
    private LeaveType seedLeaveType(String name, String description) {
        return leaveTypeRepository.findByName(name).orElseGet(() -> {
            LeaveType leaveType = LeaveType.builder()
                    .name(name)
                    .description(description)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            System.out.println("✅ LeaveType created: " + name);
            return leaveTypeRepository.save(leaveType);
        });
    }

    // =========================
    // EMPLOYEE
    // =========================
    private Employee seedEmployee(
            String employeeCode,
            String firstName,
            String lastName,
            String email,
            String phone,
            LocalDate hireDate,
            EmployeeGender gender,
            EmployeeStatus status,
            Department department,
            Position position,
            EmployeeType employeeType
    ) {
        return employeeRepository.findByEmployeeCode(employeeCode).orElseGet(() -> {
            Employee employee = Employee.builder()
                    .employeeCode(employeeCode)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .phone(phone)
                    .hireDate(hireDate)
                    .gender(gender)
                    .status(status)
                    .department(department)
                    .position(position)
                    .employeeType(employeeType)
                    .user(null)
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            System.out.println("✅ Employee created: " + employeeCode + " - " + firstName + " " + lastName);
            return employeeRepository.save(employee);
        });
    }
}