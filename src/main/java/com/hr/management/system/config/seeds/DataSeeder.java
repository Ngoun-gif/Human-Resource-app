package com.hr.management.system.config.seeds;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hr.management.system.config.seeds.department.DepartmentSeeder;
import com.hr.management.system.config.seeds.employee.EmployeeSeeder;
import com.hr.management.system.config.seeds.employee_type.EmployeeTypeSeeder;
import com.hr.management.system.config.seeds.leave_type.LeaveTypeSeeder;
import com.hr.management.system.config.seeds.permission.PermissionSeeder;
import com.hr.management.system.config.seeds.position.PositionSeeder;
import com.hr.management.system.config.seeds.role.RoleSeeder;
import com.hr.management.system.config.seeds.user.UserSeeder;
import com.hr.management.system.modules.department.entity.Department;
import com.hr.management.system.modules.employee_type.entity.EmployeeType;
import com.hr.management.system.modules.permission.entity.Permission;
import com.hr.management.system.modules.position.entity.Position;
import com.hr.management.system.modules.role.entity.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleSeeder roleSeeder;
    private final PermissionSeeder permissionSeeder;
    private final UserSeeder userSeeder;
    private final DepartmentSeeder departmentSeeder;
    private final PositionSeeder positionSeeder;
    private final EmployeeTypeSeeder employeeTypeSeeder;
    private final LeaveTypeSeeder leaveTypeSeeder;
    private final EmployeeSeeder employeeSeeder;

    @Override
    public void run(String... args) {
        Role adminRole = roleSeeder.seedRole("ADMIN", "System Administrator");
        roleSeeder.seedRole("HR", "Human Resource");

        Set<Permission> allPermissions = permissionSeeder.seedPermissions();
        permissionSeeder.assignPermissionsToRole(adminRole, allPermissions);

        userSeeder.seedAdminUser(adminRole);

        Department hrDepartment = departmentSeeder.seedDepartment("HR", "Human Resources");
        Department itDepartment = departmentSeeder.seedDepartment("IT", "Information Technology");
        Department financeDepartment = departmentSeeder.seedDepartment("FINANCE", "Finance Department");

        Position managerPosition = positionSeeder.seedPosition("MGR", "Manager", "Management role");
        Position officerPosition = positionSeeder.seedPosition("OFF", "Officer", "Operational role");
        Position staffPosition = positionSeeder.seedPosition("STAFF", "Staff", "General staff role");

        EmployeeType fullTimeType = employeeTypeSeeder.seedEmployeeType("FULL_TIME", "Full Time Employee");
        EmployeeType internType = employeeTypeSeeder.seedEmployeeType("INTERN", "Intern");

        leaveTypeSeeder.seedLeaveType("ANNUAL", "Annual Leave");
        leaveTypeSeeder.seedLeaveType("SICK", "Sick Leave");
        leaveTypeSeeder.seedLeaveType("UNPAID", "Unpaid Leave");
        leaveTypeSeeder.seedLeaveType("MATERNITY", "Maternity Leave");

        employeeSeeder.seedDefaultEmployees(
                hrDepartment,
                itDepartment,
                financeDepartment,
                managerPosition,
                officerPosition,
                staffPosition,
                fullTimeType,
                internType
        );

        System.out.println("✅ Base data seeded");
    }
}