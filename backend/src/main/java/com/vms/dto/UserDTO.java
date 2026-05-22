package com.vms.dto;

import com.vms.entity.User;

public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private User.Role role;
    private boolean enabled;

    public UserDTO() {}

    public UserDTO(Long id, String email, String fullName, User.Role role, boolean enabled) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.enabled = enabled;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
