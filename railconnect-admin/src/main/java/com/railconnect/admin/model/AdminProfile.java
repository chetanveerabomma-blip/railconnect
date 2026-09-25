package com.railconnect.admin.model;

import com.railconnect.auth.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class AdminProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(name = "employee_id", nullable = false, unique = true, length = 50)
    private String employeeId;

    @Column(name = "access_level", length = 30)
    private String accessLevel = "SUPER_ADMIN";

    public AdminProfile() {}

    public AdminProfile(User user, String department, String employeeId, String accessLevel) {
        this.user = user;
        this.department = department;
        this.employeeId = employeeId;
        this.accessLevel = accessLevel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
}
