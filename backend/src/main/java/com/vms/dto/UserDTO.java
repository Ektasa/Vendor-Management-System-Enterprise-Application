package com.vms.dto;

import com.vms.entity.User;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private User.Role role;
    private boolean enabled;
}

