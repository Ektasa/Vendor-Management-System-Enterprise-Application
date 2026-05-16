package com.vms.dto;

import com.vms.entity.User;
import jdk.jshell.Snippet;

public class UserDTO {


    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class userDTO {
        private Long id;
        private String email;
        private String fullName;
        private User.Role role;
        private boolean enabled;
    }
}

